package com.univ.tours.apa.demo;

import android.app.ProgressDialog;

import com.univ.tours.apa.R;
import com.univ.tours.apa.activities.MainActivity;
import com.univ.tours.apa.entities.Activity;
import com.univ.tours.apa.entities.Course;
import com.univ.tours.apa.entities.Session;
import com.univ.tours.apa.entities.Structure;
import com.univ.tours.apa.entities.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class DatabaseSeeder {
	private static ProgressDialog loadingDialog;

	// Setting up the DB for the demo
	public static void setupDatabaseForDemo() {
		new Thread(() -> {
			List<User> users = MainActivity.db.userDao().getAll();
			if (users.size() == 0) {
				User newUser1 = new User();
				newUser1.setLastName("Berthelette");
				newUser1.setFirstName("Nicolas");
				newUser1.setBirthday(LocalDate.now().minusYears(30).minusDays(Math.round(Math.random() * 100)));
				newUser1.setEmail("nicolas.berthelette@gmail.com");
				newUser1.setPassword("123456");
				newUser1.setRole("ROLE_PATIENT");
				newUser1.setPhoneNumber("0127514857");
				newUser1.setId(MainActivity.db.userDao().insert(newUser1));

				User newUser2 = new User();
				newUser2.setLastName("Quinn");
				newUser2.setFirstName("Amabella");
				newUser2.setBirthday(LocalDate.now().minusYears(35).minusDays(Math.round(Math.random() * 100)));
				newUser2.setEmail("amabella.quinn@gmail.com");
				newUser2.setPassword("123456");
				newUser2.setRole("ROLE_DOCTOR");
				newUser2.setPhoneNumber("0420624666");
				newUser2.setId(MainActivity.db.userDao().insert(newUser2));

				User newUser3 = new User();
				newUser3.setLastName("Rocher");
				newUser3.setFirstName("Aubrey");
				newUser3.setBirthday(LocalDate.now().minusYears(40).minusDays(Math.round(Math.random() * 100)));
				newUser3.setEmail("aubrey.rocher@gmail.com");
				newUser3.setPassword("123456");
				newUser3.setRole("ROLE_COLLABORATOR");
				newUser3.setPhoneNumber("0520604965");
				newUser3.setId(MainActivity.db.userDao().insert(newUser3));

				User newUser4 = new User();
				newUser4.setLastName("Laberge");
				newUser4.setFirstName("Ranger");
				newUser4.setBirthday(LocalDate.now().minusYears(43).minusDays(Math.round(Math.random() * 100)));
				newUser4.setEmail("ranger.laberge@gmail.com");
				newUser4.setPassword("123456");
				newUser4.setRole("ROLE_DOCTOR");
				newUser4.setPhoneNumber("0143884453");
				newUser4.setId(MainActivity.db.userDao().insert(newUser4));

				User newUser5 = new User();
				newUser5.setLastName("Legault");
				newUser5.setFirstName("Cendrillon");
				newUser5.setBirthday(LocalDate.now().minusYears(47).minusDays(Math.round(Math.random() * 100)));
				newUser5.setEmail("cendrillon.legault@gmail.com");
				newUser5.setPassword("123456");
				newUser5.setRole("ROLE_PATIENT");
				newUser5.setPhoneNumber("0143884453");
				newUser5.setId(MainActivity.db.userDao().insert(newUser5));

				User newUser6 = new User();
				newUser6.setLastName("Duffet");
				newUser6.setFirstName("Archard");
				newUser6.setBirthday(LocalDate.now().minusYears(32).minusDays(Math.round(Math.random() * 100)));
				newUser6.setEmail("archard.duffet@gmail.com");
				newUser6.setPassword("123456");
				newUser6.setRole("ROLE_COLLABORATOR");
				newUser6.setPhoneNumber("0562101634");
				newUser6.setId(MainActivity.db.userDao().insert(newUser6));

				Course course1 = new Course();
				course1.setTitle("Parcours 1");
				course1.setDescription("Description 1");
				course1.setCategory("Catégorie 1");
				course1.setDoctor(newUser2);
				course1.setPatient(newUser1);
				course1.setId(MainActivity.db.courseDao().insert(course1));

				Course course2 = new Course();
				course2.setTitle("Parcours 2");
				course2.setDescription("Description 2");
				course2.setCategory("Catégorie 2");
				course2.setDoctor(newUser4);
				course2.setPatient(newUser5);
				course2.setId(MainActivity.db.courseDao().insert(course2));

				Activity activity1 = new Activity();
				activity1.setTitle("Activité 1 détaillée");
				activity1.setDescription("Description 1 détaillée");
				activity1.setCollaborator(newUser3);
				activity1.setCourse(course1);
				activity1.setId(MainActivity.db.activityDao().insert(activity1));

				Activity activity2 = new Activity();
				activity2.setTitle("Activité 2 détaillée");
				activity2.setDescription("Description 2 détaillée");
				activity2.setCollaborator(newUser6);
				activity2.setCourse(course1);
				activity2.setId(MainActivity.db.activityDao().insert(activity2));

				Activity activity3 = new Activity();
				activity3.setTitle("Activité 3");
				activity3.setDescription("Description 3");
				activity3.setCourse(course1);
				activity3.setId(MainActivity.db.activityDao().insert(activity3));

				Activity activity4 = new Activity();
				activity4.setTitle("Activité 4 détaillée");
				activity4.setDescription("Description 4 détaillée");
				activity4.setCollaborator(newUser6);
				activity4.setCourse(course2);
				activity4.setId(MainActivity.db.activityDao().insert(activity4));

				Activity activity5 = new Activity();
				activity5.setTitle("Activité 5 détaillée");
				activity5.setDescription("Description 5 détaillée");
				activity5.setCollaborator(newUser3);
				activity5.setCourse(course2);
				activity5.setId(MainActivity.db.activityDao().insert(activity5));

				Structure structure1 = new Structure();
				structure1.setName("Structure 1");
				structure1.setDiscipline("Discipline 1");
				structure1.setPathologyList("Pathologie 1, pathologie 2");
				structure1.setId(MainActivity.db.structureDao().insert(structure1));

				Structure structure2 = new Structure();
				structure2.setName("Structure 2");
				structure2.setDiscipline("Discipline 2");
				structure2.setPathologyList("Pathologie 1, pathologie 3");
				structure2.setId(MainActivity.db.structureDao().insert(structure2));

				Structure structure3 = new Structure();
				structure3.setName("Structure 3");
				structure3.setDiscipline("Discipline 3");
				structure3.setPathologyList("Pathologie 2, pathologie 3, pathologie 4");
				structure3.setId(MainActivity.db.structureDao().insert(structure3));

				Session session1 = new Session();
				session1.setDateTime(LocalDateTime.of(LocalDate.now().minusDays(Math.round(Math.random() * 5)), LocalTime.of((int) (Math.round(Math.random() * 6) + 8), 0)));
				session1.setDuration((int) (Math.round(Math.random() * 3 + 1) * 30));
				session1.setStructure(structure1);
				session1.setActivity(activity1);
				session1.setCompletionRate((int) Math.round(Math.random() * 9 + 1));
				session1.setPatientFeedback("Commentaire 1");
				session1.setId(MainActivity.db.sessionDao().insert(session1));

				Session session2 = new Session();
				session2.setDateTime(LocalDateTime.of(LocalDate.now().minusDays(Math.round(Math.random() * 5)), LocalTime.of((int) (Math.round(Math.random() * 6) + 8), 0)));
				session2.setDuration((int) (Math.round(Math.random() * 3 + 1) * 30));
				session2.setStructure(structure1);
				session2.setActivity(activity1);
				session2.setCompletionRate((int) Math.round(Math.random() * 9 + 1));
				session2.setPatientFeedback("Commentaire 2");
				session2.setId(MainActivity.db.sessionDao().insert(session2));

				Session session3 = new Session();
				session3.setDateTime(LocalDateTime.of(LocalDate.now().plusDays(Math.round(Math.random() * 5)), LocalTime.of((int) (Math.round(Math.random() * 6) + 8), 0)));
				session3.setDuration((int) (Math.round(Math.random() * 3 + 1) * 30));
				session3.setStructure(structure1);
				session3.setActivity(activity1);
				session3.setId(MainActivity.db.sessionDao().insert(session3));

				Session session4 = new Session();
				session4.setDateTime(LocalDateTime.of(LocalDate.now().minusDays(Math.round(Math.random() * 5)), LocalTime.of((int) (Math.round(Math.random() * 6) + 8), 0)));
				session4.setDuration((int) (Math.round(Math.random() * 3 + 1) * 30));
				session4.setStructure(structure2);
				session4.setActivity(activity2);
				session4.setCompletionRate((int) Math.round(Math.random() * 9 + 1));
				session4.setPatientFeedback("Commentaire 4");
				session4.setId(MainActivity.db.sessionDao().insert(session4));

				Session session5 = new Session();
				session5.setDateTime(LocalDateTime.of(LocalDate.now().plusDays(Math.round(Math.random() * 5)), LocalTime.of((int) (Math.round(Math.random() * 6) + 8), 0)));
				session5.setDuration((int) (Math.round(Math.random() * 3 + 1) * 30));
				session5.setStructure(structure2);
				session5.setActivity(activity2);
				session5.setId(MainActivity.db.sessionDao().insert(session5));

				Session session6 = new Session();
				session6.setDateTime(LocalDateTime.of(LocalDate.now().plusDays(Math.round(Math.random() * 5)), LocalTime.of((int) (Math.round(Math.random() * 6) + 8), 0)));
				session6.setDuration((int) (Math.round(Math.random() * 3 + 1) * 30));
				session6.setStructure(structure3);
				session6.setActivity(activity4);
				session6.setId(MainActivity.db.sessionDao().insert(session6));

				Session session7 = new Session();
				session7.setDateTime(LocalDateTime.of(LocalDate.now().minusDays(Math.round(Math.random() * 5)), LocalTime.of((int) (Math.round(Math.random() * 6) + 8), 0)));
				session7.setDuration((int) (Math.round(Math.random() * 3 + 1) * 30));
				session7.setStructure(structure2);
				session7.setActivity(activity5);
				session7.setCompletionRate((int) Math.round(Math.random() * 9 + 1));
				session7.setPatientFeedback("Commentaire 7");
				session7.setId(MainActivity.db.sessionDao().insert(session7));

				Session session8 = new Session();
				session8.setDateTime(LocalDateTime.of(LocalDate.now().plusDays(Math.round(Math.random() * 5)), LocalTime.of((int) (Math.round(Math.random() * 6) + 8), 0)));
				session8.setDuration((int) (Math.round(Math.random() * 3 + 1) * 30));
				session8.setStructure(structure2);
				session8.setActivity(activity5);
				session8.setId(MainActivity.db.sessionDao().insert(session8));
			}
		}).start();
	}
}
