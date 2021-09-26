import socket
from urllib.parse import urlparse
import sys
import datetime




def get_http_response(url):
    content = b''
    request = b'GET /' + str.encode(urlparse(url).path) + b' HTTP/1.1\r\nHost: ' + str.encode(
        urlparse(url).netloc) + b'\r\nConnection: close\r\n\r\n'
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

    client_socket.connect((urlparse(url).netloc, 80))
    print("Client: " + client_socket.getsockname()[0] + " " + str(client_socket.getsockname()[1]))
    print("Server: " + socket.gethostbyname(urlparse(url).netloc) + " 80")
    client_socket.send(request)
    while True:
        response = client_socket.recv(1024)

        content += response
        if len(response) == 0:
            break

    client_socket.close()
    return content


def check_url_invalid(url):
    if urlparse(url).scheme == 'https':
        print("HTTPS Not Supported")
        return False
    return True


def get_http_header(response):
    header = response.decode().split('\r\n\r\n')[0]
    return header


def get_http_body(response):
    body = response.decode().split('\r\n\r\n')[1]
    return body


def get_status_code(header):
    header_list = header.split('\r\n')
    status_code = header_list[0].split(' ')[1]
    return status_code


def get_access_date(header):
    header_list = header.split('\r\n')
    for element in header_list:
        if element[0:4] == 'Date':
            element_list = element.replace(':', ' ').split(' ')

            date_access_string = element_list[3] + " " + element_list[4] + " " + element_list[5] + " " + element_list[
                6] + " " + \
                                 element_list[7] + " " + element_list[8]

            date_access_object = datetime.datetime.strptime(date_access_string, "%d %b %Y %H %M %S")
            time_interval = datetime.timedelta(hours=10)
            date_access_object = date_access_object + +time_interval

            date_access = date_access_object.strftime("%d/%m/%Y %H:%M:%S")
            return date_access


def get_modified_date(header):
    header_list = header.split('\r\n')
    date_modified = ''
    for element in header_list:
        if element[0:13] == 'Last-Modified':
            element_list = element.replace(':', ' ').split(' ')

            date_modified_string = element_list[3] + " " + element_list[4] + " " + element_list[5] + " " + element_list[
                6] + " " + \
                                   element_list[7] + " " + element_list[8]

            date_modified_object = datetime.datetime.strptime(date_modified_string, "%d %b %Y %H %M %S")
            time_interval = datetime.timedelta(hours=10)
            date_modified_object = date_modified_object + +time_interval

            date_modified = date_modified_object.strftime("%d/%m/%Y %H:%M:%S")
    return date_modified


def get_content_type(header):
    header_list = header.split('\r\n')
    content_type = ''
    for element in header_list:
        if element[0:12] == 'Content-Type':
            element_list = element.split(' ')
            content_type = element_list[1]
    return content_type


def write_content_in_file(content_type, http_body):
    if content_type == "text/plain":
        f = open('output.txt', 'w', encoding='utf-8')
        f.write(http_body)
        f.close()
    elif content_type == "text/html":
        f = open('output.html', 'w', encoding='utf-8')
        f.write(http_body)
        f.close()
    elif content_type == "text/css":
        f = open('output.css', 'w', encoding='utf-8')
        f.write(http_body)
        f.close()
    elif content_type == "text/javascript" or content_type == "application/javascript":
        f = open('output.js', 'w', encoding='utf-8')
        f.write(http_body)
        f.close()
    elif content_type == "application/json":
        f = open('output.json', 'w', encoding='utf-8')
        f.write(http_body)
        f.close()
    elif content_type == "text/octet-stream":
        f = open('output', 'w', encoding='utf-8')
        f.write(http_body)
        f.close()
    else:
        f = open('output', 'w', encoding='utf-8')
        f.write(http_body)
        f.close()



def get_new_location(header):
    header_list = header.split('\r\n')
    location = ''
    for element in header_list:
        if element[0:8] == 'Location':
            element_list = element.split(' ')
            location = element_list[1]
    return location


def main():
    url = sys.argv[1]
    print("URL Requested: " + url)

    while check_url_invalid(url):
        http_response = get_http_response(url)
        http_header = get_http_header(http_response)
        http_body = get_http_body(http_response)

        status_code = get_status_code(http_header)
        if status_code == '200':
            print("Retrieval Successful")
            access_date = get_access_date(http_header)
            modified_date = get_modified_date(http_header)
            print("Date Accessed: " + access_date + " AEST")
            content_type = get_content_type(http_header)
            if modified_date != '':
                print("Last Modified: " + modified_date + " AEST")
            else:
                print("Last Modified not available")
            write_content_in_file(content_type, http_body)
            break
        elif status_code == '301':

            url = get_new_location(http_header)
            print("Resource permanently moved to " + url)
            if check_url_invalid(url):
                continue
            else:
                break

        elif status_code == '302':

            url = get_new_location(http_header)
            print("Resource temporarily moved to " + url)
            if check_url_invalid(url):
                continue
            else:
                break
        else:
            print("Retrieval Failed (" + status_code + ")")
            break


if __name__ == '__main__':
    main()
