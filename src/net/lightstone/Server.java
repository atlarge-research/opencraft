/*
 * Copyright (c) 2010-2011 Graham Edgecombe.
 *
 * This file is part of Lightstone.
 *
 * Lightstone is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Lightstone is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Lightstone.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.lightstone;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.lightstone.io.NbtChunkIoService;
import net.lightstone.net.MinecraftPipelineFactory;
import net.lightstone.net.SessionRegistry;
import net.lightstone.task.PulseTask;
import net.lightstone.task.TaskScheduler;
import net.lightstone.world.TestWorldGenerator;
import net.lightstone.world.World;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public final class Server {

	private static final Logger logger = Logger.getLogger(Server.class.getName());

	public static void main(String[] args) {
		try {
			Server server = new Server();
			server.bind(new InetSocketAddress(25565));
			server.start();
		} catch (Throwable t) {
			logger.log(Level.SEVERE, "Error during server startup.", t);
		}
	}

	private final ServerBootstrap bootstrap = new ServerBootstrap();
	private final ChannelGroup group = new DefaultChannelGroup();
	private final ExecutorService executor = Executors.newCachedThreadPool();

	private final SessionRegistry sessions = new SessionRegistry();
	private final TaskScheduler scheduler = new TaskScheduler();
	private final World world = new World(new NbtChunkIoService(), new TestWorldGenerator());

	public Server() {
		logger.info("Starting Lightstone...");
		init();
	}

	private void init() {
		ChannelFactory factory = new NioServerSocketChannelFactory(executor, executor);
		bootstrap.setFactory(factory);

		ChannelPipelineFactory pipelineFactory = new MinecraftPipelineFactory(this);
		bootstrap.setPipelineFactory(pipelineFactory);
	}

	public void bind(SocketAddress address) {
		logger.info("Binding to address: " + address + "...");
		group.add(bootstrap.bind(address));
	}

	public void start() {
		scheduler.schedule(new PulseTask(this));
		logger.info("Ready for connections.");
	}

	public ChannelGroup getChannelGroup() {
		return group;
	}

	public SessionRegistry getSessionRegistry() {
		return sessions;
	}

	public TaskScheduler getScheduler() {
		return scheduler;
	}

	public World getWorld() {
		return world;
	}

}
