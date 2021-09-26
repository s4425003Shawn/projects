from pyspark.ml.feature import HashingTF, IDF, Tokenizer, Word2Vec
from pyspark.ml.recommendation import ALS
from pyspark.sql import SparkSession
from pyspark.sql import functions as f
from pyspark.sql.functions import explode, col, udf, concat_ws
from pyspark.sql.types import StructType, StructField, StringType, IntegerType, FloatType, ArrayType
import argparse

def dot(A, B):
    return sum(a * b for a, b in zip(A, B))

#Consine similarity fomular
def cosine_similarity(a, b):
    return dot(a, b) / ((dot(a, a) ** .5) * (dot(b, b) ** .5))

#Map id to its index with a new column
def translate(mapping):
    def translate_(col):
        return mapping.get(col)

    return udf(translate_, IntegerType())

#Map index back to id with a new column
def reverse_user_index(mapping):
    def translate_(col):
        return mapping.get(col)

    return udf(translate_, StringType())


def reverse_mention_index(x):
    return [(mention_new_dict.get(val[0]), val[1]) for val in x]



spark = SparkSession \
    .builder \
    .appName("Assignment 2") \
    .getOrCreate()

parser = argparse.ArgumentParser()
parser.add_argument("--output", help="the output path",
                        default='assignment_2')
args = parser.parse_args()
output_path = args.output

#retrive data from json file to a pyspark dataframe and cache in memory
original_data = spark.read.option("multiline", "true").json('tweets.json').cache()


"""Workload 1"""
# input hard code target user for workload 1
target_user = 55199013
#select the useful attributes user_id, retweet_id and replyto_id for workload 1 
user_interest = original_data.select("user_id", "retweet_id", "replyto_id") \
    .filter("retweet_id is not null or replyto_id is not null") # filter the  user who has never tweet or reply
user_interest.show() #[0]

#this udf customer function is use to convert an array column to the string type.
join_udf = udf(lambda x: " ".join(x))
"""Used concat_ws to conbined replyto_id and retweet_id
    and then used groupBy user_id to collect each user's replyto_id and retweet_id
    to an list in doc_rep column and finally convert list to a string."""
user_interest_collection = user_interest \
    .withColumn('Document Representation', concat_ws('', col('retweet_id'), col('replyto_id'))) \
    .drop("retweet_id", "replyto_id").groupBy('user_id') \
    .agg(f.collect_list('Document Representation').alias("doc_rep")) \
    .withColumn("doc_rep", join_udf(col("doc_rep")))
user_interest_collection.show(truncate = False) #[1]


# TF-IDF feature extractor for each user id
tokenizer = Tokenizer(inputCol="doc_rep", outputCol="words")
wordsData = tokenizer.transform(user_interest_collection)
hashingTF = HashingTF(inputCol="words", outputCol="rawFeatures", numFeatures=200)
featurizedData = hashingTF.transform(wordsData)
idf = IDF(inputCol="rawFeatures", outputCol="features")
idfModel = idf.fit(featurizedData)

#dataframe includes two column user_id and features and features
#reulted by above regular TF-IDF feature extractor progress 
tf_idf_data = idfModel.transform(featurizedData).select("user_id", "features")
tf_idf_data.show(truncate = False)#[2]

"""Cosine similarity apply"""
# Convert dataframe to rdd
rdd_feature = tf_idf_data.rdd.map(lambda row: (row.user_id, row.features))

# return the feature of the target user
target_user_feature = rdd_feature.filter(lambda rec: rec[0] == target_user).values().collect()[0]

# Assign the feature of target user to each row in rdd_feature (contains features for every user), used 
# cosine similarity fomular to assign the result to a new column. filter the row for taget user and sorted
# in descending to show the top 5 similar user.
top_five = rdd_feature.map(lambda row: (row[0], cosine_similarity(row[1], target_user_feature))).filter(
    lambda row: row[0] != target_user).sortBy(lambda row: row[1], ascending=False)

#return an id list for these 5 users.
tf_idf_result = top_five.keys().take(5)

# w2v feature extractor for each user id
tokenizer = Tokenizer(inputCol="doc_rep", outputCol="words")
wordsData = tokenizer.transform(user_interest_collection)
word2Vec = Word2Vec(vectorSize=250, minCount=0, inputCol="words", outputCol="features")
model = word2Vec.fit(wordsData)

#dataframe includes two column user_id and features and features
#reulted by above regular w2v feature extractor progress 
w2v_data = model.transform(wordsData).select("user_id", "features")
w2v_data.show(truncate = False)#[3]


"""Cosine similarity apply"""
# Convert dataframe to rdd
rdd_feature = w2v_data.rdd.map(lambda row: (row.user_id, row.features))
# return the feature of the target user
target_user_feature = rdd_feature.filter(lambda rec: rec[0] == target_user).values().collect()[0]
# Assign the feature of target user to each row in rdd_feature (contains features for every user), used 
# cosine similarity fomular to assign the result to a new column. filter the row for taget user and sorted
# in descending to show the top 5 similar user
top_five = rdd_feature.map(lambda row: (row[0], cosine_similarity(row[1], target_user_feature))) \
    .filter(lambda row: row[0] != target_user).sortBy(lambda row: row[1], ascending=False)

#return an id list for these 5 users.
w2v_result = top_five.keys().take(5)

"""Workload 2"""
#select the useful attributes user_id, user_mentions.id for workload 1 
user_mentions_pair = original_data.select(col("user_id").alias("tweet_user"),
                                          col("user_mentions.id").alias("mention_user"))
user_mentions_pair.show(truncate = False)#[4]

# Explode the array of mention_user to single value and composite with tweet_user a pari value. Then
# group tweet_user and mention_user to count how many times a user mentioned a particular user.
rating_calculate = user_mentions_pair.withColumn('mention_user', explode("mention_user")) \
    .groupBy("tweet_user", "mention_user").count() \
    .withColumn("rating", col("count").cast(FloatType())).drop("count")

rating_calculate.show(truncate = False)#[5]
# create a map dictionary between tweet user id and its index
t_user_index = rating_calculate.select("tweet_user").distinct().rdd.map(
    lambda x: x[0]).zipWithIndex().collectAsMap()
# create a map dictionary between mention user id and its index
m_user_index = rating_calculate.select("mention_user").distinct().rdd.map(
    lambda x: x[0]).zipWithIndex().collectAsMap()

# Apply index for tweet_user and mention_user to new two columns user_index and mention_index
als_pre = rating_calculate.withColumn("user_index", translate(t_user_index)("tweet_user")).withColumn(
    "mention_index", translate(m_user_index)("mention_user"))
als_pre.show(truncate = False)#[6]

# Build the recommendation model using ALS on als_pre data
# Note we set cold start strategy to 'drop' to ensure we don't get NaN evaluation metrics
als = ALS(maxIter=5, regParam=0.01, userCol="user_index", itemCol="mention_index", ratingCol="rating",
          coldStartStrategy="drop")
model = als.fit(als_pre)
# predictions = model.transform(test)
# # Generate top 5 mention recommendations for each user
userRecs = model.recommendForAllUsers(5)

userRecs.show()#[7]
# reverse index back to id
tweet_new_dict = dict(zip(t_user_index.values(), t_user_index.keys()))
mention_new_dict = dict(zip(m_user_index.values(), m_user_index.keys()))

#Create struct to save mention_user and prodiction pair
recommendation_struct = StructType(
    [StructField("mention_user", StringType(), True), StructField("prediction", StringType(), True)])
# custom function to add a reversed column for recommendation
recommendation_udf = udf(lambda y: reverse_mention_index(y), ArrayType(recommendation_struct))

# result the final out put for 5 recommendation for each user id.
result = userRecs.withColumn("tweet_user", reverse_user_index(tweet_new_dict)("user_index")).withColumn(
    "top 5 recommendations", recommendation_udf(userRecs.recommendations)).drop("recommendations").drop(
    "user_index")
result.show()#[8]

# output a json file
result.coalesce(1).write.json(output_path)
print("TF-IDF feature extractor result list")
print(tf_idf_result)
print("Word2Vec feature extractor result list")
print(w2v_result)
spark.stop()