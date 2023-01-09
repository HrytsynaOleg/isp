function generatePass() {

  var randomstring = Math.random().toString(36).slice(-8);

    document.getElementById('inputPassword').value=randomstring;
};
