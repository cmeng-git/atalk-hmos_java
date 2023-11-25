/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license. See terms of license at gnu.org.
 */
package org.atalk.hmos.gui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.preference.PreferenceScreen;

import net.java.sip.communicator.util.UtilActivator;

import org.atalk.hmos.R;
import org.atalk.hmos.gui.util.PreferenceUtil;
import org.atalk.service.configuration.ConfigurationService;
import org.atalk.service.osgi.OSGiActivity;
import org.atalk.service.osgi.OSGiPreferenceFragment;

/**
 * Chat security settings screen with Omemo preferences - modified for aTalk
 *
 * @author Pawel Domas
 * @author Eng Chong Meng
 */
public class ChatSecuritySettings extends OSGiActivity {
    // OMEMO Security section
    static private final String P_KEY_OMEMO_KEY_BLIND_TRUST = "pref.key.omemo.key.blind.trust";

    static private ConfigurationService mConfig = null;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            // Display the fragment as the main content.
            getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
        }
        setMainTitle(R.string.service_gui_settings_MESSAGING_SECURITY_TITLE);
    }

    /**
     * The preferences fragment implements Omemo settings.
     */
    public static class SettingsFragment extends OSGiPreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener {
        /**
         * {@inheritDoc}
         */
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            super.onCreatePreferences(savedInstanceState, rootKey);
            addPreferencesFromResource(R.xml.security_preferences);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onStart() {
            super.onStart();

            mConfig = UtilActivator.getConfigurationService();
            PreferenceScreen screen = getPreferenceScreen();
            PreferenceUtil.setCheckboxVal(screen, P_KEY_OMEMO_KEY_BLIND_TRUST,
                    mConfig.getBoolean(mConfig.PNAME_OMEMO_KEY_BLIND_TRUST, true));

            SharedPreferences shPrefs = getPreferenceManager().getSharedPreferences();
            shPrefs.registerOnSharedPreferenceChangeListener(this);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onStop() {
            SharedPreferences shPrefs = getPreferenceManager().getSharedPreferences();
            shPrefs.unregisterOnSharedPreferenceChangeListener(this);
            super.onStop();
        }

        /**
         * {@inheritDoc}
         */
        public void onSharedPreferenceChanged(SharedPreferences shPreferences, String key) {
            if (key.equals(P_KEY_OMEMO_KEY_BLIND_TRUST)) {
                mConfig.setProperty(mConfig.PNAME_OMEMO_KEY_BLIND_TRUST,
                        shPreferences.getBoolean(P_KEY_OMEMO_KEY_BLIND_TRUST, true));
            }
        }
    }
}
