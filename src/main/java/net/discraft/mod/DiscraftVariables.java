package net.discraft.mod;

import net.discraft.mod.notification.ClientNotification;

import java.util.ArrayList;

public class DiscraftVariables {

    public int playerCount = 0;

    /**
     * First Start Check
     */
    public boolean firstStart = false;

    /**
     * Swing Animations
     */
    public float swing;
    public float smoothSwing;

    /**
     * Client Notifications
     */
    public ArrayList<ClientNotification> clientNotificationList = new ArrayList<ClientNotification>();
    public ClientNotification currentClientNotification = null;
    /**
     * PVP Rear Cam Variables
     */
    public boolean rearCamHasChecked = false;
    public int reconnectionTimer = 10;
    public int initialReconnectionTimer = 300;
    public boolean lastConnected;

}
