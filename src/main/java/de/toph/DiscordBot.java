package de.toph;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.constant.CommandConstant;
import de.toph.database.LiteSQL;
import de.toph.listener.CommandListener;
import de.toph.quartz.QuartzScheduler;
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
 * @author Tophhhhh
 *
 */
public class DiscordBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBot.class);

    private static DiscordBot INSTANCE;

    private JDABuilder builder;

    private JDA jda;

    private QuartzScheduler scheduler;

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
	Config conf = Config.getInstance();
	scheduler = new QuartzScheduler();

	LiteSQL.connect(conf.getDbPath());

	builder = JDABuilder.createDefault(conf.getKey())
		.enableIntents(EnumSet.allOf(GatewayIntent.class))
		.setActivity(Activity.playing("Disco Party Sahne"))
		.setStatus(OnlineStatus.ONLINE)
		.addEventListeners(new CommandListener());

	jda = builder.build();
	jda.updateCommands().addCommands(commands()).queue();
	LOGGER.info("Bot started!!");
//	try {
//	    scheduler.start();
//	    scheduler.init();
//	} catch (SchedulerException e) {
//	    e.printStackTrace();
//	}
    }

    /**
     * get a list of commands
     * 
     * @return commandlist
     */
    private List<CommandData> commands() {
	List<CommandData> commandlist = new ArrayList<>();
	// Move all command
	commandlist.add(Commands
		.slash(CommandConstant.MOVEALLCOMMAND, "move all from current voice Channel into an other channel")
		.setGuildOnly(true).addOption(OptionType.CHANNEL, "channel", "channel to which should be moved", true)
		.setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.VOICE_MOVE_OTHERS)));

	// Coin flip command

	commandlist.add(Commands.slash(CommandConstant.COINFLIPCOMMAND, "play coinflip!").setGuildOnly(true));

	// Weather command
	commandlist.add(Commands.slash(CommandConstant.WEATHERCOMMAND, "get Weather").setGuildOnly(true));

	// Poll command currently disabled
//	commandlist.add(Commands.slash(CommandConstant.POLLCOMMAND, "create poll")
//		.addOption(OptionType.STRING, "question", "set a question")
//		.setGuildOnly(true));

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
