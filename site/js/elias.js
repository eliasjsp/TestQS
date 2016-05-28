$(function () {
    function populateWhatIDoList(key, val) {
        var ul = $("#"+key);
        $.each(val, function (key, val) {
            ul.append("<li>" + val + "</li>");
        });
    }

    function populateEducationTimeline(key, val) {
        var div = $("#"+key);
        var i = 0;
        $.each(val, function ( key, val ) {
            div.append(' <li ' + (i%2 == 0 ? '' : 'class="timeline-inverted"') + '> ' +
                            '<div class="posted-date"> ' +
                                '<span class="month">' + val.when + '</span> ' +
                            '</div><!-- /posted-date --> ' +
                            '<div class="timeline-panel wow fadeInUp"> ' +
                                '<div class="timeline-content"> ' +
                                    '<div class="timeline-heading"> ' +
                                        '<h3>' + val.what + '</h3> ' +
                                        '<span>' + val.where + '</span> ' +
                                    '</div><!-- /timeline-heading --> ' +
                                    '<div class="timeline-body"> ' +
                                        '<p>' + val.desc + '</p> ' +
                                    '</div><!-- /timeline-body --> ' +
                                '</div> <!-- /timeline-content --> ' +
                            '</div><!-- /timeline-panel --> ' +
                        '</li>');
            i++;
        });
    }

    function populateOtherSkills(key, val) {
        var div = $("#"+key);
        $.each(val, function ( key, val ) {
            div.append('<div class="col-xs-12 col-sm-4 col-md-2"> '+
                            '<div class="chart" data-percent="' + val.progress + '" data-color="e74c3c">'+
                                '<span class="percent"></span>'+
                                '<div class="chart-text">'+
                                    '<span>' + val.name + '</span>'+
                                '</div>'+
                            '</div>'+
                        '</div>');
        });
    }

    function populateSkills(key, val) {
        var i = 0;
        var div = $("#"+key);
        var divA = "<div class=\"col-md-6\">";
        var divB = divA;
        $.each(val, function (key, val) {
            if((i%2) == 0){
                divA += '<div class="skill-progress">' +
                            '<div class="skill-title"><h3>' + val.name + '</h3></div>'+
                            '<div class="progress">'+
                                '<div class="progress-bar six-sec-ease-in-out" role="progressbar" aria-valuenow="'+val.progress+'" aria-valuemin="0" aria-valuemax="100" style="width:'+val.progress+'%;"><span>'+val.progress+'%</span>'+
                                '</div>'+
                            '</div>'+
                        '</div>';
            } else {
                divB += '<div class="skill-progress">' +
                            '<div class="skill-title"><h3>' + val.name + '</h3></div>'+
                            '<div class="progress">'+
                                '<div class="progress-bar six-sec-ease-in-out" role="progressbar" aria-valuenow="'+val.progress+'" aria-valuemin="0" aria-valuemax="100" style="width:'+val.progress+'%;"><span>'+val.progress+'%</span>'+
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

    $.getJSON( "../json/elias.json", function( data ) {
        $.each(data, function (key, val) {
            //console.log("key : " + key + " val: " + val);
            switch (key) {
                case "home-facebook" :
                case "home-linkedin" :
                    $("#"+key).attr("href", val);
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
                    if(val == 0)
                        $("#btn-hire").css( 'display', 'none' );
                    break;
                default:
                    $("#"+key).append(val);

            }
        });
    });
});
