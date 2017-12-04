package duoxianchengshejimoshi.f4guarded;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;

public class AlarmAgent {
    private volatile boolean connectedToServer = false;
    
    private final Predicate agentConnected = new Predicate() {
        
        @Override
        public boolean evaluate() {
            
            return connectedToServer;
        }
    };
    
    private final Blocker blocker = new ConditionVarBlocker();
    
    private final Timer heartbeatTimer = new Timer(true);
    
    public void sendAlarm(final AlarmInfo alarm) throws Exception {
        GuardedAction<Void> guardedAction = new GuardedAction<Void>(agentConnected) {
            
            @Override
            public Void call() throws Exception {
                dosendAlarm(alarm);
                return null;
            }
        };
        
        blocker.callWithGuard(guardedAction);
    }
    
    private void dosendAlarm(AlarmInfo alarm) {
        System.out.println("sending alarm " + alarm);
        try {
            Thread.sleep(50);
        } catch (Exception e) {
        }
    }
    
    public void init() {
        Thread connectingThread = new Thread(new ConnectingTask());
        connectingThread.start();
        heartbeatTimer.schedule(new HeartbeatTask(), 60000, 2000);
    }
    
    public void disconnect() {
        System.out.println("disconnect from alarm server.");
        connectedToServer = false;
    }
    
    protected void onConnected() {
        try {
            blocker.signalAfter(new Callable<Boolean>() {
                
                @Override
                public Boolean call() {
                    connectedToServer = true;
                    System.out.println("connected to server");
                    return Boolean.TRUE;
                }
            });
        } catch (Exception e) {
        }
    }
    
    protected void onDisconnected() {
        connectedToServer = false;
    }
    
    private class ConnectingTask implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onConnected();
        }
        
        
    }
    
    private class HeartbeatTask extends TimerTask {

        @Override
        public void run() {
            if (!testConnection()) {
                onDisconnected();
                reconnect();
            }
        }
        
        private boolean testConnection() {
            return true;
        }
        
        private void reconnect() {
            ConnectingTask connectingThread = new ConnectingTask();
            connectingThread.run();
        }
    }

    public static void main(String[] args) {
        AlarmAgent aa = new AlarmAgent();
        aa.init();
    }
}

class AlarmInfo {
    private String info = "warning!!";

    public String getInfo() {
        return info;
    }

}


