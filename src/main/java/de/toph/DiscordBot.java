package de.toph;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.database.LiteSQL;
import de.toph.service.IService;
import de.toph.service.ServiceImpl;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

/**
 * 
 * @author Toph Discord Bot
 */
public class DiscordBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBot.class);

    private static DiscordBot INSTANCE;

    private JDABuilder builder;

    private JDA jda;

    private IService service;
    
    private Config config;
    
    public DiscordBot() {
	config = new Config();
	service = new ServiceImpl();
    }
    
    /**
     * the instance of Discord bot
     * 
     * @return instance
     * @throws LoginException
     */
    public static DiscordBot getInstance() throws LoginException {
	if (INSTANCE == null) {
	    INSTANCE = new DiscordBot();
	}
	return INSTANCE;
    }
    
    /**
     * execute Discord Bot
     * 
     * @param args
     */
    public void execute(String[] args) {
	LiteSQL.connect(config.getDbPath());

	builder = JDABuilder.createDefault(config.getKey()).enableIntents(EnumSet.allOf(GatewayIntent.class));
	builder.setActivity(Activity.playing("Looking for friends!"));
	builder.setStatus(OnlineStatus.ONLINE);

	jda = service.getBuilderWithEventListener();
	jda.updateCommands().addCommands(commands()).queue();
    }

    /**
     * get a list of commands
     * 
     * @return commandlist
     */
    private List<CommandData> commands() {
	List<CommandData> commandlist = new ArrayList<>();
	// SLASH COMMANDS
	commandlist.add(Commands.slash("moveall", "move all from current voice Channel into an other channel")
		.addOption(OptionType.CHANNEL, "channel", "channel to which should be moved", true)
		.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_MOVE_OTHERS)));

	
//	commandlist.add(Commands.slash("support", "<Dummy>"));
//	commandlist.add(Commands.slash("closeticket", "<Dummy>"));
	// CONTEXT COMMANDS

	return commandlist;
    }

    /**
     * get Builder
     * 
     * @return builder
     */
    public JDABuilder getBuilder() {
	return builder;
    }

    /**
     * get Jda
     * 
     * @return jda
     */
    public JDA getJda() {
	return jda;
    }
}
