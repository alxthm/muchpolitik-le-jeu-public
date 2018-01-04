package com.muchpolitik.lejeu.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.user.client.Window;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration((int) (Window.getClientWidth() * 0.8f), (int) (Window.getClientHeight() * 0.8f));
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new LeJeuHtml();
        }
}