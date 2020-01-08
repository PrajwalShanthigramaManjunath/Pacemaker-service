package controllers;

import java.util.*;

import com.google.common.base.Optional;

import models.Activity;
import models.User;


public class PacemakerAPI {

    private Map<String, User> emailIndex = new HashMap<>();
    private Map<String, User> userIndex = new HashMap<>();
    private Map<String, Activity> activitiesIndex = new HashMap<>();
    public Map<String, Set<User>> friendIndex = new HashMap();
    public User loggedInUser = null;

    public PacemakerAPI() {
    }

    public Collection<User> getUsers() {
        return userIndex.values();
    }


    public User createUser(String firstName, String lastName, String email, String password) {
        User user = new User(firstName, lastName, email, password);
        emailIndex.put(email, user);
        userIndex.put(user.id, user);
        return user;
    }

    public Activity createActivity(String id, String type, String location, double distance) {
        Activity activity = null;
        Optional<User> user = Optional.fromNullable(userIndex.get(id));
        if (user.isPresent()) {
            activity = new Activity(type, location, distance);
            user.get().activities.put(activity.id, activity);
            activitiesIndex.put(activity.id, activity);
        }
        return activity;
    }

    public Activity getActivity(String id) {
        return activitiesIndex.get(id);
    }


    public void addFriend(String id, String email) {
        Set<User> friends = friendIndex.get(id);
        User user = getUserByEmail(email);
        Set<User> friend = new HashSet<>();
        if (friendIndex.get(id) == null) {
            friend.add(user);
            friendIndex.put(id, friend);
        } else {
            friends.add(user);
            friendIndex.put(id, friends);
        }
    }

    public List<Activity> listActivities(String userId, String sortBy) {
        List<Activity> activities = new ArrayList<>();
        activities.addAll(userIndex.get(userId).activities.values());
        switch (sortBy) {
            case "type":
                activities.sort((a1, a2) -> a1.type.compareTo(a2.type));
                break;
            case "location":
                activities.sort((a1, a2) -> a1.location.compareTo(a2.location));
                break;
            case "distance":
                activities.sort((a1, a2) -> Double.compare(a1.distance, a2.distance));
                break;
        }
        return activities;
    }


    public User getUserByEmail(String email) {
        return emailIndex.get(email);
    }

    public List<Activity> listFriendActivities(String email) {
        List<Activity> friendActivities = new ArrayList<>();
        User user = getUserByEmail(email);
        if (user != null) {
            for (Map.Entry<String, Activity> entry : user.activities.entrySet()) {
                friendActivities.add(entry.getValue());
            }
            Collections.sort(friendActivities, Comparator.comparing(o -> o.type));
        }
        return friendActivities;
    }

    public User getUser(String id) {
        return userIndex.get(id);
    }

    public Set<User> getFriends(String id) {
        return friendIndex.get(id);
    }

}
