function loadRepoGroups() {
    $.ajax(
        'api/v1/repository-groups',
        {
            type: 'GET',
            success: function (data) {
                // remove existing and
                $("#repositoryGroups").empty();
                $.each(data,
                    function (i, item) {
                        $("#repositoryGroups").append(
                            '<li data-element-id="' + item.id + '"><a href="#">' + item.name + '<span class="caret"></span></a><ul class="nav collapse" id="submenu1" role="menu" aria-labelledby="btn-1" aria-expanded="true"></ul></li>');
                    });

            }
        }).error(function () {
            //console.log("error");
        });
}

function searchByRepo(event) {
    var element = $(event.currentTarget);
    var searchVal = element.val();
    if (searchVal.length > 3) {
        $.ajax(

            'api/v1/repository-groups/repository-name/'+ searchVal,
            {
                type: 'GET',
                success: function (data) {
                    // remove existing and
                    $("#repositoryGroups").empty();
                    $.each(data,
                        function (i, item) {
                            $("#repositoryGroups").append(
                                '<li data-element-id="' + item.id + '"><a href="#">' + item.name +'<span class="caret"></span></a><ul class="nav collapse" id="submenu1" role="menu" aria-labelledby="btn-1" aria-expanded="true"></ul></li>');
                        });

                }
            }).error(function () {
                //console.log("error");
            });
    }
    if(element.val().length == 0){
        loadRepoGroups();
    }
}

function loadRepoGroup(event) {
    var element = $(event.currentTarget).parent();
    var id = element.data("element-id");
    //element.parent.find("li").removeClass("active");
    $.ajax(
        'api/v1/repository-groups/' + id,
        {
            type: 'GET',
            success: function (data) {
                element.find("ul").empty();
                $.each(data.repositories,
                    function (i, item) {
                        element.find("ul").append($('<li data-element-id="' + item.id + '"/>').html('<a class="reponavigation" href="#">' + item.name + '</a>'));
                    });
                element.find("ul").toggleClass("in");

                element.addClass("active");
            }
        }).error(function () {
            //console.log("error");
        });
}
function loadRepo(event) {

    var element = $(event.currentTarget).parent();
    var id = element.data("element-id");
    $.ajax(
        'api/v1/repositories/' + id,
        {
            type: 'GET',
            success: function (data) {
                var source   = $("#repo-template").html();
                var template = Handlebars.compile(source);
                var context = {data: data};
                var html    = template(context);
                $("#repoDetails").html(html);
                $('#resulttable').DataTable( {
                    "processing": true,
                    "serverSide": true,
                    "ajax": "api/v1/logs",
                    // "ajax": "api/v1/repositories/1/branch/{selected-branch}",
                    "columns": [
                        { "data": "action" },
                        { "data": "stage" },
                        { "data": "repositoryName" },
                        { "data": "repositoryGroupName" },
                        { "data": "branch" },
                        { "data": "version" }
                    ]
                });
                $('.datepicker').datetimepicker();
            }
        }).error(function () {
            //console.log("error");
        });
}
