function loadRepositoryGroups() {
    $.ajax(
        'api/v1/repository-groups',
        {
            type: 'GET',
            success: function (data) {
                $("#repositoryGroups").empty();
                if(data) {
                    $.each(data,
                        function (i, item) {
                            $("#repositoryGroups").append(
                                '<li data-element-id="' + item.id + '"><a href="#">' + item.name + '<span class="caret"></span></a><ul class="nav collapse" id="submenu1" role="menu" aria-labelledby="btn-1" aria-expanded="true"></ul></li>');
                        }
                    );
                }
            }
        }).error(function () {
        //console.log("error");
    });
}

function searchByRepository(event) {
    var element = $(event.currentTarget);
    var searchVal = element.val();
    if (searchVal && searchVal.length >= 3) {
        $.ajax(
            'api/v1/repository-groups/repository-name/' + searchVal,
            {
                type: 'GET',
                success: function (data) {
                    $("#repositoryGroups").empty();
                    if(data) {
                        $.each(data,
                            function (i, item) {
                                var repogroupHtml = '<li data-element-id="' + item.id + '"><a href="#">' + item.name + '<span class="caret"></span></a>' +
                                    '<ul class="nav collapse in" id="submenu1" role="menu" aria-labelledby="btn-1" aria-expanded="true">';
                                if(item.repositories) {
                                    $.each(item.repositories,
                                        function (i, repository) {
                                            repogroupHtml += '<li data-element-id="' + repository.id + '"><a class="reponavigation" href="#">' + repository.name + '</a></li>';
                                        }
                                    );
                                }
                                repogroupHtml += "</ul></li>";
                                $("#repositoryGroups").append(repogroupHtml);
                            }
                        );
                    }
                }
            }).error(function () {
            //console.log("error");
        });
    }
    if (element.val().length == 0) {
        loadRepositoryGroups();
    }
}

function loadRepositoryGroup(event) {
    var searchString = $('#searchfield').val();
    var element = $(event.currentTarget).parent();
    var id = element.data("element-id");
    if(!searchString && searchString.length < 3) {
        $.ajax(
            'api/v1/repository-groups/' + id,
            {
                type: 'GET',
                success: function (data) {
                    element.find("ul").empty();
                    if (data) {
                        $.each(data.repositories,
                            function (i, item) {
                                element.find("ul").append($('<li data-element-id="' + item.id + '"/>').html('<a class="reponavigation" href="#">' + item.name + '</a>'));
                            }
                        );
                    }
                }
            }).error(function () {
            //console.log("error");
        });
    }
    element.find("ul").toggleClass("in");
}

function loadRepository(event) {
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
                var url = 'api/v1/statuses?repositoryId='+ repositoryId;
                if (branchId) {
                    url += '&branchId=' + branchId;
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

function clearLogSearchFields(event) {
    var $select = $('#action').selectize();
    var control = $select[0].selectize;
    control.clear();
    var $select = $('#stage').selectize();
    var control = $select[0].selectize;
    control.clear();

    $('#stage').val('');
    $('#repositoryName').val('');
    $('#repositoryGroupName').val('');
    $('#branch').val('');
    $('#version').val('');
    $('#user').val('');
    $('#auftrag').val('');
    $('#lowerDate').val('');
    $('#upperDate').val('');
    searchLogs(event);
}

function searchLogs(event){
    var action = $("#action").val();
    var stage = $("#stage").val();
    var repositoryName = $("#repositoryName").val();
    var repositoryGroupName = $("#repositoryGroupName").val();
    var branch = $("#branch").val();
    var version = $("#version").val();
    var user = $("#user").val();
    var auftrag = $("#auftrag").val();
    var lowerDate = $("#lowerDate").val();
    var upperDate = $("#upperDate").val();

    var query = '?1=1';
    if (action) {
        query += "&action=" + action;
    }
    if (stage) {
        query += "&stage=" + stage;
    }
    if (repositoryName) {
        query += "&repositoryName=" + repositoryName;
    }
    if (repositoryGroupName) {
        query += "&repositoryGroupName=" + repositoryGroupName;
    }
    if (branch) {
        query += "&branch=" + branch;
    }
    if (version) {
        query += "&version=" + version;
    }
    if (user) {
        query += "&user=" + user;
    }
    if (auftrag) {
        query += "&auftrag=" + auftrag;
    }
    if (lowerDate) {
        query += "&lowerDate=" + moment(lowerDate, 'DD.MM.YYYY hh:mm').format("YYYY-MM-DDTHH:mm:ss");
    }
    if (upperDate) {
        query += "&upperDate=" + moment(upperDate, 'DD.MM.YYYY hh:mm').format("YYYY-MM-DDTHH:mm:ss");
    }

    datatable.destroy();
    datatable = $('#resulttable').DataTable( {
        "processing": true,
        "serverSide": true,
        "searching": false,
        "ajax": "api/v1/logs"+query,
        "columns": [
            { "data": "action" },
            { "data": "stage" },
            { "data": "repositoryName" },
            { "data": "repositoryGroupName" },
            { "data": "branch" },
            { "data": "version" },
            { "data": "userId" },
            { "data": "auftragNr" },
            { "data": "logdate" }
        ],
        "columnDefs": [{
            targets: 8,
            render: $.fn.dataTable.render.moment('YYYY-MM-DDTHH:mm:ss','DD.MM.YYYY HH:mm')
        }]
    } );
}

function initDropdowns() {
    $('#action').selectize({
        preload: true,
        valueField: 'name',
        labelField: 'name',
        sortField: 'name',
        searchField: 'name',
        create: false,
        render: {
            option: function(item, escape) {
                return '<div class="option" value="' + item.name + '">' + item.name + '</div>';
            }
        },
        load: function(query, callback) {
            $.ajax({
                url: 'api/v1/logs/actions',
                type: 'GET',
                dataType: 'json',
                error: function () {
                    callback();
                },
                success: function (res) {
                    callback(res);
                }
            });
        },
        onChange: function(value) {
            $('#searchButton').click();;
        }
    });
    $('#stage').selectize({
        preload: true,
        valueField: 'name',
        labelField: 'name',
        sortField: 'name',
        searchField: 'name',
        create: false,
        render: {
            option: function(item, escape) {
                return '<div class="option" value="' + item.name + '">' + item.name + '</option>';
            }
        },
        load: function(query, callback) {
            $.ajax({
                url: 'api/v1/logs/stages',
                type: 'GET',
                dataType: 'json',
                error: function () {
                    callback();
                },
                success: function (res) {
                    callback(res);
                }
            });
        },
        onChange: function(value) {
            $('#searchButton').click();;
        }
    });
}
