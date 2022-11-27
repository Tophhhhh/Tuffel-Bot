package de.toph.service;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.toph.DiscordBot;
import de.toph.listener.ClearMessageListener;
import de.toph.listener.MessageOfMonthListener;
import de.toph.listener.MoveAllListener;
import de.toph.listener.RandomMessages;
import de.toph.listener.RoleListener;
import de.toph.listener.TempVoiceListener;
import de.toph.listener.VerifyListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

/**
 * 
 * @author Toph
 *
 */
public class ServiceImpl implements IService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceImpl.class);

    /**
     * 
     * Set up new Events
     * 
     * @return events
     */
    private List<Object> getEvents() {
	List<Object> events = new ArrayList<>();
	// L I S T E N E R
	events.add(new TempVoiceListener());
	events.add(new RoleListener());

	// S L A S H
	events.add(new MessageOfMonthListener());
	events.add(new VerifyListener());
	events.add(new MoveAllListener());
	events.add(new ClearMessageListener());

	events.add(new RandomMessages());
	return events;
    }

    /**
     * append the events and build the jda
     */
    @Override
    public JDA getBuilderWithEventListener() {
	try {
	    List<Object> events = getEvents();
	    JDABuilder builder = DiscordBot.getInstance().getBuilder();
	    for (Object event : events) {
		builder.addEventListeners(event);
	    }

//			// Listener
//			builder.addEventListeners(new TempVoiceListener());
//			builder.addEventListeners(new RoleListener());
//
//			// Slash
//			builder.addEventListeners(new MessageOfMonthListener());
//			builder.addEventListeners(new VerifyListener());
////			builder.addEventListeners(new ClearMessageListener());
//			builder.addEventListeners(new MoveAllListener());
	    return builder.build();
	} catch (LoginException e) {
	    logger.error(e.getMessage(), e);
	}
	return null;
    }

}
