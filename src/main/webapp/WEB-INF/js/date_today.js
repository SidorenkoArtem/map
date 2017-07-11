/*  в календаре установить 
 *   сегодняшнюю дату 
 *  в input выбора диапазона
 */
document.addEventListener('DOMContentLoaded', function(){
    var d = new Date();
    var day = d.getDate();
    var month = d.getMonth() + 1;
    var year = d.getFullYear();
    var name_input = document.getElementById('today1')
    name_input.value = day + "-" + month + "-" + year;
});

document.addEventListener('DOMContentLoaded', function(){
    var d = new Date();
    var day = d.getDate();
    var month = d.getMonth() + 1;
    var year = d.getFullYear();
    var name_input = document.getElementById('today2')
    name_input.value = day + "-" + month + "-" + year;
});