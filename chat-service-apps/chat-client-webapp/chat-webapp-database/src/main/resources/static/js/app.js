$(document).ready(
		function() {
			var notifications = '/messages';
			var socket = new SockJS(notifications);
			var client = Stomp.over(socket);
			var source = $("#message-template").html();
			var template = Handlebars.compile(source);
			var events = [];
			client.connect({}, function(frame) {
				client.subscribe("/topic/message", function(message) {
					var event = JSON.parse(message.body);
					console.error('event: ' + event);
					// put in the collection
					events.push(event);
					// only show the top 20
					events.slice(Math.max(events.length - 50, 1))
					// get the content from the template
					var content = template({
						eventList : events
					});
					// update the content from the template to the UI
					var messageWindow = document.getElementById("eventList");
					messageWindow.innerHTML = content;
					// scroll to the bottom
					messageWindow.scrollTop = messageWindow.scrollHeight;
				});
			}, function(error) {
				console.log("STOMP protocol error " + error);
			});
		}
		
		


);
