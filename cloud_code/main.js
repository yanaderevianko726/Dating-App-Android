//////// Send push notifications ///////
Parse.Cloud.define("sendPush", async (request) => {

  var userQuery = new Parse.Query(Parse.User);
  userQuery.equalTo("objectId", request.params.receiverId);

  // Find devices associated with these users
  var pushQuery = new Parse.Query(Parse.Installation);
  pushQuery.matchesQuery('user', userQuery);

  return Parse.Push.send({
    //channels: ["global"],
      where: pushQuery, // Set our Installation query
        data: {
          alert: request.params.alert,
          title: request.params.title,
          senderId: request.params.senderId,
          senderName: request.params.senderName,
          type: request.params.type,
          chat: request.params.chat,
          avatar: request.params.avatar
        }
    }, {
      useMasterKey: true
    })
    .then(function () {
      // Push sent!
      return "sent";
    }, function (error) {
      // There was a problem :(
        return Promise.reject(error);
    });

});


////// Send gift ///////
Parse.Cloud.define("send_gift", async request => {

  var objectId = request.params.objectId;
  var credits = request.params.credits;

  var userQuery = new Parse.Query(Parse.User);
  userQuery.equalTo("objectId", objectId);

  const user = await userQuery.first({
    useMasterKey: true
  });

  user.increment("tokens", credits);

  return user.save(null, {
      useMasterKey: true
    }).then(function () {

      return "updated"
    })
    .catch(function (error) {

      return Promise.reject(error);

    });

});
