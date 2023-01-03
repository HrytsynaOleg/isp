//const myAlert = document.getElementById('myAlert')
//myAlert.addEventListener('closed.bs.alert', event => {
  // do something, for instance, explicitly move focus to the most appropriate element,
  // so it doesn't get lost/reset to the start of the page
  // document.getElementById('...').focus()
//})

function edit() {
    document.getElementById('inputEmail').disabled=false;
    document.getElementById('inputName').disabled=false;
    document.getElementById('inputLastName').disabled=false;
    document.getElementById('inputPhone').disabled=false;
    document.getElementById('inputAdress').disabled=false;
    document.getElementById('saveButton').style.display = "inline";
    document.getElementById('cancelButton').style.display = "inline";
    document.getElementById('editButton').style.display = "none";
};
function cancel() {
    document.getElementById('inputEmail').disabled=true;
    document.getElementById('inputName').disabled=true;
    document.getElementById('inputLastName').disabled=true;
    document.getElementById('inputPhone').disabled=true;
    document.getElementById('inputAdress').disabled=true;
    document.getElementById('saveButton').style.display = "none";
    document.getElementById('cancelButton').style.display = "none";
    document.getElementById('editButton').style.display = "inline";
    document.getElementById('inputEmail').value="{sessionScope.loggedUser.email}";
};