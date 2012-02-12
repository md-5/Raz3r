package com.md_5.raz3r;

public class Raz3r extends org.bukkit.plugin.java.JavaPlugin implements org.bukkit.event.Listener {

    private final org.pircbotx.PircBotX bot = new org.pircbotx.PircBotX();

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        new Thread() {

            @Override
            public void run() {
                try {
                    bot.setLogin("Raz3r");
                    bot.connect(getConfig().getString("host"));
                    bot.changeNick(getConfig().getString("nick"));
                    bot.joinChannel(getConfig().getString("channel"));
                    bot.getListenerManager().addListener(new org.pircbotx.hooks.ListenerAdapter<org.pircbotx.PircBotX>() {

                        @Override
                        public void onMessage(final org.pircbotx.hooks.events.MessageEvent<org.pircbotx.PircBotX> event) throws Exception {
                            if (!event.getUser().getNick().equalsIgnoreCase(event.getBot().getNick())) {
                                getServer().broadcastMessage("[IRC]<" + event.getUser().getNick() + "> " + event.getMessage());
                            }
                        }
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @org.bukkit.event.EventHandler(priority = org.bukkit.event.EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerChat(final org.bukkit.event.player.PlayerChatEvent event) {
        bot.sendMessage(getConfig().getString("channel"), org.bukkit.ChatColor.stripColor(String.format(event.getFormat(), event.getPlayer().getDisplayName(), event.getMessage())));
    }
}
