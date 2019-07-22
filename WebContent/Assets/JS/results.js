function setIcon(nodeId, type){
	$("#jstree").jstree(true).set_icon(nodeId, BASE_URL+"/Assets/images/treeIcons/"+type+".png");
}

function addNode(parent){
	var children= parent.data.children;
	for (var i =0; i< children.length; i++ ){
		var data ={
				"id" : children[i].name,
				"text" : children[i].name,
				"data": children[i]
		};
		$('#jstree').jstree().create_node(parent.id, data, "last", function() {
			setIcon(data.id, children[i].nodeType);
		});

	}
	parent.data=[];

}



function createTree(commentTree){

	var data = [ {
		"id" : commentTree.name,
		"parent" : "#",
		"text" : commentTree.name,
		"data":commentTree
	}];
	$("#jstree").jstree({
		"core" : {
			// so that create works
			"check_callback" : true,
			"data" : data
		},
		"plugins" : [ "contextmenu", "dnd", "themes","html_data","dnd","ui","types" ],

		'types': {
			"valid_children" : [ "package" ],
			'types' : {
				'package' : {
					'icon' : {
						'image' : 'https://clintonvillewichamber.com/wp-content/uploads/2018/01/search-icon.png'
					}
				},
				'default' : {
					'icon' : {
						'image' : '/admin/views/images/file.png'
					},
					'valid_children' : 'default'
				}
			}

		},

		"contextmenu" : {
			"items" : {
				"create" : {
					"label" : "Add",
					"action" : function(obj) {
						$('#jstree').jstree().create_node('#', {
							"id" : "ajson5",
							"text" : "newly added"
						}, "last", function() {
							alert("done");
						});
					},
				}
			}
		}

	}).on('create_node.jstree', function(e, data) {
		//setIcon(data.node.id, data.node.nodeType);
	}).on("select_node.jstree", function (e, data) { 
		addNode(data.node);
	});
}
$(function(){	

	function ajaxResults()
	{

		$.ajax(
				{
					url: ANALYSIS_URL,
					type:'post',
					data:"",

					success:function(data){ 
						var jsonData = JSON.parse(data);
						createTree(jsonData);
					},
					error:function(data){ 

					},

					contentType:false,
					processData: false 
				})
	}

	$( document ).ready(function(e){
		ajaxResults();
	});
});




