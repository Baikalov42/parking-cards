function sendAjaxRequest() {
    var brand = $("#brand").val();
    $.get( "/api/models/get-by-brand/" + brand, function( data ) {
        $("#model").empty();
        for (const [key, value] of Object.entries(data)) {
            var option = "<option value = " + key + ">" + value +  "</option>";
            $("#model").append(option);
        }
        $("#model-select").show();
        $("#placeholder").remove();
    });
};
$(document).ready(function() {
    $("#brand").change(function() {
        sendAjaxRequest();
    });
});

$(function () {
    $('#submit-button').click(function (e) {        
        e.preventDefault();

        let formData = new FormData(ajaxform);
        let object = {};
        formData.forEach(function(value, key){
            object[key] = value;
        });
        let json = JSON.stringify(object);
        console.log(json);
        let endpoint = document.getElementById('ajaxform').getAttribute('action');
        let method = document.getElementById('ajaxform').getAttribute('ajaxmethod');
        let xhr = new XMLHttpRequest();
        xhr.open(method, endpoint);
        xhr.setRequestHeader('Content-Type', 'application/json');
        xhr.send(json);
        xhr.onload = function() {
            if (xhr.status != 200) {
                let msg = JSON.parse(xhr.response).detailedMessage;
                $('#msgs').html("<div class='alert alert-danger'>"+"Error! "+ msg +"</div>");
            } else {
                $('#msgs').html("<div class='alert alert-success'> Done: "+xhr.response+"</div>");
                function redirect() {
                    let link = document.getElementById('back').getAttribute('href');
                    window.location.href = link;
                }
                setTimeout(redirect, 1000);
            }
        }
        
    })
});

$(function () {
    $('.delete').click(function (e) {        
        e.preventDefault();
        let btn = e.currentTarget;
        let row = document.getElementById(btn.getAttribute('itemid'));
        let endpoint = btn.href;
        let xhr = new XMLHttpRequest();
        xhr.open('delete', endpoint);
        xhr.send();
        xhr.onload = function() {
            if (xhr.status != 200) {
                let msg = JSON.parse(xhr.response).detailedMessage;
                row.innerHTML = "<p style='color: green'>"+"Error! "+ msg +"</p>";
            } else {
                row.innerHTML = "<p style='color: green'>Deleted!</p>";
            }
        }
    });
});