package controllers;

import io.javalin.Context;
import models.Activity;
import models.Location;
import models.User;

import static models.Fixtures.users;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PacemakerRestService {

	PacemakerAPI pacemaker = new PacemakerAPI();

	PacemakerRestService() {
		users.forEach(
				user -> pacemaker.createUser(user.firstname, user.lastname, user.email, user.password));
	}

	public void listUsers(Context ctx) {
		ctx.json(pacemaker.getUsers());
	}


	public void createUser(Context ctx) {
		User user = ctx.bodyAsClass(User.class);
		User newUser = pacemaker
				.createUser(user.firstname, user.lastname, user.email, user.password);
		ctx.json(newUser);
	}

	public void listUser(Context ctx) {
		String id = ctx.pathParam("id");
		ctx.json(pacemaker.getUser(id));
	}

	public void getActivities(Context ctx) {
		String id = ctx.pathParam("id");
		User user = pacemaker.getUser(id);
		if (user != null) {
			ctx.json(user.activities.values());
		} else {
			ctx.status(404);
		}
	}

	public void getFriends(Context ctx) {
		String id = ctx.pathParam("id");
		Set<User> user = pacemaker.getFriends(id);
		if (user != null) {
			List<User> FriendList = new ArrayList<User>(user);
			ctx.json(FriendList);
		} else {
			ctx.status(404);
		}
	}

	public void getFriendActivities(Context ctx) {
		String email = ctx.pathParam("email");
		List<Activity> activities = pacemaker.listFriendActivities(email);

		if (activities != null) {
			ctx.json(activities);
		} else {
			ctx.status(404);
		}
	}


	public void createActivity(Context ctx) {
		String id = ctx.pathParam("id");
		User user = pacemaker.getUser(id);
		if (user != null) {
			Activity activity = ctx.bodyAsClass(Activity.class);
			Activity newActivity = pacemaker
					.createActivity(id, activity.type, activity.location, activity.distance);
			ctx.json(newActivity);
		} else {
			ctx.status(404);
		}
	}

	public void getActivity(Context ctx) {
		String id = ctx.pathParam("activityid");
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
			ctx.json(activity);
		} else {
			ctx.status(404);
		}
	}

	public void getActivityLocations(Context ctx) {
		String id = ctx.pathParam("activityid");
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
			ctx.json(activity.route);  
		} else {
			ctx.status(404);
		}
	}

	public void addLocation (Context ctx) {
		String id = ctx.pathParam("activityid");
		Activity activity = pacemaker.getActivity(id);
		if (activity != null) {
			Location location = ctx.bodyAsClass(Location.class);
			activity.route.add(location);
			ctx.json(location);
		}else {
			ctx.status(404);
		}
	}


	public void addFriend(Context ctx){
		String id = ctx.pathParam("id");
		String email = ctx.pathParam("email");
		if(id != null && email != null){
			pacemaker.addFriend(id, email);
		}

	}
}