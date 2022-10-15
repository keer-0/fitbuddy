document.addEventListener("DOMContentLoaded", () => {
	onLoaded();
});

function onLoaded() {
	console.log("Page loaded.");
}

function onSubmit() {
	let name = document.forms["registerForm"]["name"].value;
	let password = document.forms["registerForm"]["password"].value;
	let passwordConfirm = document.forms["registerForm"]["passwordConfirm"].value;
	
	name = name.trim();
	password = password.trim();
	passwordConfirm = passwordConfirm.trim();
	
	let url = "/register";
	let data = { 	name : name,
					password : password,
					passwordConfirm : passwordConfirm };
					
	let xhr = new XMLHttpRequest();
	xhr.open("POST", url);
	xhr.setRequestHeader("Content-Type", "application/json; charset=UTF-8");			
	xhr.send(JSON.stringify(data));
	
	xhr.onreadystatechange = function() {
		if (this.readyState == XMLHttpRequest.DONE) {
			if (this.status == 200) {
				// SUCCESS
				console.log("OK: " + this.responseText);	
			} else {
				// ERROR
				console.log("ERROR: " + this.responseText);		
			}
			//showStatus(this.responseText);
			clearRegisterForm();		
		}		
	}
}

function clearRegisterForm() {
	clearFormValue("registerForm", "name");
	clearFormValue("registerForm", "password");
	clearFormValue("registerForm", "passwordConfirm");	
}