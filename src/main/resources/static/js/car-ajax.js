$(function () {
    $('#submit-button').click(function (e) {        
        //Prevent default submission of form
        e.preventDefault();
        
        //Remove previous errors
        // $('input').next('span').remove();
        let formData = new FormData(newcarform);
        let object = {};
        formData.forEach(function(value, key){
            object[key] = value;
        });
        let json = JSON.stringify(object);
        console.log(json);
        let xhr = new XMLHttpRequest();
        xhr.open('POST', '/api/cars');
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(json);
        xhr.onload = function() {
            if (xhr.status != 200) {
                let msg = JSON.parse(xhr.response).detailedMessage;
                $('#msgs').html("<div class='alert alert-danger'>"+"Error! "+ msg +"</div>");
            } else {
                $('#msgs').html("<div class='alert alert-success'>"+xhr.response+"</div>");
            }
        }
        
    })
});