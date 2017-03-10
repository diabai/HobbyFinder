package diabai.uw.tacoma.edu.hobbyfinder;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Instrumentation test for Dashboard. This class launches the dashboardActivity which then launches the edit profile activity and
 * fills the editTexts then clicks submit.
 * @Author: Ibrahim Diabate
 * @version: 2.0
 *
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class DashboardEditProfileTest {


    @Rule
    public ActivityTestRule<Dashboard> mActivityRule = new ActivityTestRule<>(
            Dashboard.class);


    @Test
    public void testEditProfile() {

        onView(withId(R.id.btn_homepage))
                .perform(click());

      onView(withText("Edit profile"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));

        // Type text and then press the button.
        onView(withId(R.id.edit_user_name))
                .perform(typeText("Instru test"));
        onView(withId(R.id.edit_email))
                .perform(typeText("Instru_email@yahoo.com"));
        onView(withId(R.id.edit_hometown))
                .perform(typeText("Tacoma"));
        onView(withId(R.id.add_hobbies_frag_button))
                .perform(click());

    }








}
