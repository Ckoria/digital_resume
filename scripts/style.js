// To break out of iframe and access the parent window
const streamlitDoc = window.parent.document;

// Make the replacement
document.addEventListener("DOMContentLoaded", function(event){
        streamlitDoc.getElementsByTagName("footer")[0].innerHTML = "Provided by <a href='https://yourwebsite.com' target='_blank' class='css-z3au9t egzxvld2'>Your Link Display Text Here</a>";
    });