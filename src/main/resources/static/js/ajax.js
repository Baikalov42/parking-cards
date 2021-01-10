function sendAjaxRequest() {
    var brand = $("#brand").val();
    $.get( "/api/models/get-by-brand/" + brand, function( data ) {
        $("#model").empty();
        for (const [key, value] of Object.entries(data)) {
            var option = "<option value = " + key + ">" + value +  "</option>";
            $("#model").append(option);
        }
        $("#model").addClass("vis");
        $("#model-label").addClass("vis");
    });
};
$(document).ready(function() {
    $("#brand").change(function() {
        sendAjaxRequest();
    });
});