console.log("This is Script File.")


//Sidebar Close or Open
const toggleSidebar = () =>{
	if($(".sidebar").is(":visible")){
		//true
		//close the sidebar
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}
	else{
		//false
		//open the sidebar
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
};



//Search Functionality
const search = ( )=> {
	//console.log("searching...");
	
	let query = $("#search-input").val();
	
	if(query == ""){
		$(".search-result").hide();
	}else{
		//search
		console.log(query);
		let url = `http://localhost:9000/search/${query}`;     //This is backtick
		
		fetch(url)
			.then((response) =>{
				return response.json();
			})
			.then((data) =>{
				//console.log(data);
				let text=`<div class='list-group'>`;
				
				data.forEach((contact)=>{
					text += `<a href='/user/${contact.cid}/contact' class='list-group-item list-group-item-action'> ${contact.name} </a>`;
				});
				text +=`</div>`;
				
				$(".search-result").html(text);
				$(".search-result").show();
		});	
		$(".search-result").show();		
	}
};