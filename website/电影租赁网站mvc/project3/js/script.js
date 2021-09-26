function validatePassword(password) {

	// Do not show anything when the length of password is zero.
	if (password.length === 0) {
		document.getElementById("msg").innerHTML = "";
		return;
	}
	// Create an array and push all possible values that you want in password
	var matchedCase = new Array();
	matchedCase.push("[$@$!%*#?&]"); // Special Charector
	matchedCase.push("[A-Z]"); // Uppercase Alpabates
	matchedCase.push("[0-9]"); // Numbers
	matchedCase.push("[a-z]"); // Lowercase Alphabates

	// Check the conditions
	var ctr = 0;
	for (var i = 0; i < matchedCase.length; i++) {
		if (new RegExp(matchedCase[i]).test(password)) {
			ctr++;
		}
	}
	// Display it
	var color = "";
	var strength = "";
	switch (ctr) {
		case 0:
		case 1:
		case 2:
			strength = "Very Weak";
			color = "red";
			break;
		case 3:
			strength = "Medium";
			color = "orange";
			break;
		case 4:
			strength = "Strong";
			color = "green";
			break;
	}
	document.getElementById("msg").innerHTML = strength;
	document.getElementById("msg").style.color = color;
}


$(document).ready(function () {







	(function () {
		'use strict';
		window.addEventListener('load', function () {
			// Fetch all the forms we want to apply custom Bootstrap validation styles to
			var forms = document.getElementsByClassName('needs-validation');
			// Loop over them and prevent submission
			var validation = Array.prototype.filter.call(forms, function (form) {
				form.addEventListener('submit', function (event) {
					if (form.checkValidity() === false) {
						event.preventDefault();
						event.stopPropagation();
					}
					form.classList.add('was-validated');
				}, false);
			});
		}, false);
	})();
});
