package com.elisoft.kache_conductor;

import com.elisoft.kache_conductor.servicio.Servicio_eliminar;

/**
 * Contiene las constantes de las acciones de los servicios y sus parámetros
 */
public class Constants {


    /**
     * Constantes para {@link Servicio_eliminar}
     */
    public static final String ACTION_RUN_ISERVICE = "com.elisoft.valle_grande_conductor.action.RUN_INTENT_SERVICE";
    public static final String ACTION_PROGRESS_EXIT = "com.elisoft.valle_grande_conductor.action.PROGRESS_EXIT";

    public static final String EXTRA_PROGRESS = "com.elisoft.valle_grande_conductor.extra.PROGRESS";



    public static final int NOTIFICATION_ID_FOREGROUND_SERVICE = 8466503;

    public static class ACTION {
        public static final String MAIN_ACTION = "test.action.main";
        public static final String START_ACTION = "test.action.start";
        public static final String STOP_ACTION = "test.action.stop";
    }

    public static class STATE_SERVICE {
        public static final int CONNECTED = 10;
        public static final int NOT_CONNECTED = 0;
        public static final int GPS_INACTIVO = 1;
        public static final int SIN_INTERNET = 2;
    }
}
