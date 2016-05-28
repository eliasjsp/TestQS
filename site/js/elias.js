$(function () {
    function populateWhatIDoList(key, val) {
        var ul = $("#"+key);
        $.each(val, function (key, val) {
            ul.append("<li>" + val + "</li>");
        });
    }

    function populateEducationTimeline(key, val) {

    }

    function populateOtherSkills(key, val) {

    }

    function populateSkills(key, val) {
        var i = 0;
        console.log(key);
        var div = $("#"+key);
        var divA = "<div class=\"col-md-6\">";
        var divB = divA;
        $.each(val, function (key, val) {
            if((i%2) == 0){
                divA += '<div class="skill-progress">' +
                            '<div class="skill-title"><h3>' + val.name + '</h3></div>'+
                            '<div class="progress">'+
                                '<div class="progress-bar six-sec-ease-in-out" role="progressbar" aria-valuenow="'+val.progress+'" aria-valuemin="0" aria-valuemax="100" ><span>'+val.progress+'%</span>'+
                                '</div>'+
                            '</div>'+
                        '</div>';
            } else {
                divB += '<div class="skill-progress">' +
                            '<div class="skill-title"><h3>' + val.name + '</h3></div>'+
                            '<div class="progress">'+
                                '<div class="progress-bar six-sec-ease-in-out" role="progressbar" aria-valuenow="'+val.progress+'" aria-valuemin="0" aria-valuemax="100" ><span>'+val.progress+'%</span>'+
                                '</div>'+
                            '</div>'+
                        '</div>';
            }
            i++;
        });
        if(val.length > 0) {
            divA += "</div>";
            div.append(divA);
        }
        if(val.length > 1) {
            divB += "</div>";
            div.append(divB);
        }
    }

    function populateExperienceTimeline(key, val) {

    }

    $.getJSON( "../elias.json", function( data ) {
        $.each(data, function (key, val) {
            //console.log("key : " + key + " val: " + val);
            switch (key) {
                case "home-facebook" :
                case "home-linkedin" :
                    $("#"+key).href = val;
                    break;
                case "what-i-do-list":
                    populateWhatIDoList(key, val);
                    break;
                case "programming-skills":
                    populateSkills(key, val);
                    break;
                case "other-skills":
                    populateOtherSkills(key, val);
                    break;
                case "education-timeline":
                    populateEducationTimeline(key, val);
                    break;
                case "experience-timeline":
                    populateExperienceTimeline(key, val);
                    break;
                case "hire-available":
                    $("#btn-hire").style = (key == 0 ? "display: none;" : "");
                    break;
                default:
                    $("#"+key).append(val);

            }
        });
    });
});
