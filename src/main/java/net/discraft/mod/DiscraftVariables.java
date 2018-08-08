package net.discraft.mod;

import net.discraft.mod.notification.ClientNotification;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiscraftVariables {

    /**
     * First Start Check
     */
    public boolean firstStart = false;

    /**
     * Swing Animations
     */
    public float swing;

    /**
     * Client Notifications
     */
    public ArrayList<ClientNotification> clientNotificationList = new ArrayList<ClientNotification>();
    public ClientNotification currentClientNotification = null;

    /**
     * Gui Variables
     */
    public float lastReachF = 0;
    public double lastReachD = 0;
    public boolean isMouseDown = false;
    public double playerSpeed;
    public int clicksPerSecondRefreshRate = 3;
    public int clicksPerSecond = 0;
    public int clicksPerSecondAverage = 0;
    /**
     * PVP Rear Cam Variables
     */
    public boolean rearCamHasChecked = false;
    public int reconnectionTimer = 10;
    public int initialReconnectionTimer = 300;
    public boolean lastConnected;
    public boolean clicksPerSecondHasPressed;
    List<Integer> clicksPerSecondList = new ArrayList<Integer>();

}
