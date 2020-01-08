package controllers;

import io.javalin.Javalin;

public class RestMain {

	public static void main(String[] args) {
		Javalin app = Javalin.create()
				.start(getHerokuAssignedPort());
		PacemakerRestService service = new PacemakerRestService();
		configRoutes(app, service);
	}

	private static int getHerokuAssignedPort() {
		String herokuPort = System.getenv("PORT");
		if (herokuPort != null) {
			return Integer.parseInt(herokuPort);
		}
		return 7000;
	}

	static void configRoutes(Javalin app, PacemakerRestService service) {

		app.get("/users", ctx -> {
			service.listUsers(ctx);
		});

		app.post("/users", ctx -> {
			service.createUser(ctx);
		});

		app.get("/users/:id", ctx -> {
			service.listUser(ctx);
		});

		app.get("/users/:id/activities", ctx -> {
			service.getActivities(ctx);
		});

		app.post("/users/:id/activities", ctx -> {
			service.createActivity(ctx);
		});

		app.put("/users/:id/friend/:email", ctx -> {
			service.addFriend(ctx);
		});

		app.get("/users/:id/friends", ctx -> {
			service.getFriends(ctx);
		});

		app.get("/users/:id/activities/:activityid", ctx -> {
			service.getActivity(ctx);
		});

		app.get("/users/:id/activities/:activityid/locations", ctx -> {
			service.getActivityLocations(ctx);
		});

		app.post("/users/:id/activities/:activityid/locations", ctx -> {
			service.addLocation(ctx);
		});
		app.get("/users/activities/friend/:email", ctx -> {
			service.getFriendActivities(ctx);
		});

	}
}