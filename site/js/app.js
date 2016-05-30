$(function () {
    var resumeCount = 0;
    function populateWhatIDoList(key, val) {
        var ul = $("#"+key);
        $.each(val, function (key, val) {
            ul.append("<li>" + val + "</li>");
        });
    }

    function getResumeHtml(val) {
        return ' <li ' + (resumeCount % 2 == 0 ? '' : 'class="timeline-inverted"') + '> ' +
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
            '</li>';
    }

    function populateResumeTimeline(key, val) {
        var div = $("#"+key);
        $.each(val, function ( key, val ) {
            div.append(getResumeHtml(val));
            resumeCount++;
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
    
     function populatePublishedApps(key, val) {
         var div = $("#"+key);
         var publishedAppDiv;
          $.each(val, function (key, val) {
               publishedAppDiv = 
                        '<div class="col-md-4 text-center thumb-wrapper">' +
                            '<div class="demo-thumb">' +
                            '<a href="' + val.link + '" title="Always on Display"> ' +
                                '<img src="' + val["image-url"] + '" style="max-width:200px; max-height:200px" alt="Color Point">' +
                            '</a>' +
                            '</div>' +
                            '<a href="' + val.link + '" class="btn btn-primary" style="margin-top: 10px;">Color Point</a>' +
                        '</div>';
                        
               div.append(publishedAppDiv);
          });
     }

    $.getJSON( "../json/" + window.location.href.split("name=")[1].split(".")[0] + ".json", function( data ) {
        $.each(data, function (key, val) {
            switch (key) {
                case "title":
                    document.title = val;
                    $(".intro-sub").html(val);
                    break;
                case "home-facebook":
                case "home-linkedin":
                    $("#" + key).attr("href", val);
                    break;
				case "photo":
					$("#" + key).attr("src", val);
					$("#" + key).attr("alt", val.split("/").slice(-1)[0].split(".")[0]);
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
                case "experience-timeline":
                    populateResumeTimeline(key, val);
                    break;
                case "hire-available":
                    if(val == 0)
                        $("#btn-hire").css('display', 'none');
                    break;
                case "published-apps":
                    populatePublishedApps(key, val);
                    break;
                default:
                    $("#" + key).append(val);
            }
        });
    });
});
