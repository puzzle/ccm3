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
    if (searchVal.length > 2) {
        $.ajax(
            'api/v1/repository-groups/repository-name/' + searchVal,
            {
                type: 'GET',
                success: function (data) {
                    $("#repositoryGroups").empty();
                    $.each(data,
                        function (i, item) {
                            var repogroupHtml = '<li data-element-id="' + item.id + '"><a href="#">' + item.name + '<span class="caret"></span></a>' +
                                '<ul class="nav collapse in" id="submenu1" role="menu" aria-labelledby="btn-1" aria-expanded="true">';
                            $.each(item.repositories,
                                function (i, repository) {
                                    repogroupHtml += '<li data-element-id="' + repository.id + '"><a class="reponavigation" href="#">' + repository.name + '</a></li>';
                                }
                            );
                            repogroupHtml += "</ul></li>";

                            $("#repositoryGroups").append(repogroupHtml);
                        }
                    );
                }
            }).error(function () {
            //console.log("error");
        });
    }
    if (element.val().length == 0) {
        loadRepoGroups();
    }
}

function loadRepoGroup(event) {
    var element = $(event.currentTarget).parent();
    var id = element.data("element-id");
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
            }
        }).error(function () {
        //console.log("error");
    });
}

function loadRepo(event) {
    var element = $(event.currentTarget).parent();
    var repositoryId = element.data("element-id");
    $('.reponavigation.active').removeClass('active');
    $(event.currentTarget).addClass("active");
    $.ajax(
        'api/v1/repositories/' + repositoryId,
        {
            type: 'GET',
            success: function (data) {
                var source = $("#repo-template").html();
                var template = Handlebars.compile(source);
                var context = {data: data};
                var html = template(context);
                var branchId = $("#branch").val();
                if (branchId) {
                    var url = 'api/v1/statuses?repositoryId='+ repositoryId +'&branchId=' + branchId;
                } else {
                    var url = 'api/v1/statuses';
                }

                $("#repoDetails").html(html);
                $("#resulttable").DataTable({
                    "processing": true,
                    "serverSide": true,
                    "ajax": url,
                    "searching": false,
                    "order": [[6, 'desc']],
                    "columns": [
                        {"data": "branch.name"},
                        {"data": "stage"},
                        {"data": "version"},
                        {"data": "userId"},
                        {"data": "status"},
                        {"data": "auftragNr"},
                        {"data": "executed"}
                    ],
                    "columnDefs": [{
                        targets: 6,
                        render: $.fn.dataTable.render.moment('YYYY-MM-DDTHH:mm:ss','DD.MM.YYYY HH:mm')
                    }]
                });
                $('.datepicker').datetimepicker();
            }
        }).error(function () {
        //console.log("error");
    });
}

function loadBranch(event) {
    var repositoryId = $("li[data-repository-id]").attr("data-repository-id");
    var branchId = $("#branch").val();
    var url = 'api/v1/statuses?repositoryId='+ repositoryId;
    if (branchId) {
        url = url + '&branchId=' + branchId;
    }
    $('#resulttable').DataTable().ajax.url(url).load();
}
