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

package net.lightstone.model;

import net.lightstone.msg.CompressedChunkMessage;
import net.lightstone.msg.Message;

/**
 * Represents a chunk of the map.
 * @author Graham Edgecombe
 */
public final class Chunk {

    /**
     * The radius (not including the current chunk) of the chunks that the
     * player can see.
     */
	public static final int VISIBLE_RADIUS = 8;

    /**
     * A chunk key represents the X and Z coordinates of a chunk and implements
     * the {@link #hashCode()} and {@link #equals(Object)} methods making it
     * suitable for use as a key in a hash table or set.
     * @author Graham Edgecombe
     */
	public static final class Key {

        /**
         * The coordinates.
         */
		private final int x, z;

        /**
         * Creates a new chunk key with the specified X and Z coordinates.
         * @param x The X coordinate.
         * @param z The Z coordinate.
         */
		public Key(int x, int z) {
			this.x = x;
			this.z = z;
		}

        /**
         * Gets the X coordinate.
         * @return The X coordinate.
         */
		public int getX() {
			return x;
		}

        /**
         * Gets the Z coordinate.
         * @return The Z coordinate.
         */
		public int getZ() {
			return z;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + z;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (x != other.x)
				return false;
			if (z != other.z)
				return false;
			return true;
		}

	}

    /**
     * The dimensions of a chunk.
     */
	public static final int WIDTH = 16, HEIGHT = 16, DEPTH = 128;

    /**
     * The coordinates of this chunk.
     */
	private final int x, z;

    /**
     * The data in this chunk representing all of the blocks and their state.
     */
	private final byte[] types, metaData, skyLight, blockLight;

    /**
     * Creates a new chunk with a specified X and Z coordinate.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     */
	public Chunk(int x, int z) {
		this.x = x;
		this.z = z;
		this.types = new byte[WIDTH * HEIGHT * DEPTH];
		this.metaData = new byte[WIDTH * HEIGHT * DEPTH];
		this.skyLight = new byte[WIDTH * HEIGHT * DEPTH];
		this.blockLight = new byte[WIDTH * HEIGHT * DEPTH];
	}

    /**
     * Gets the X coordinate of this chunk.
     * @return The X coordinate of this chunk.
     */
	public int getX() {
		return x;
	}

    /**
     * Gets the Z coordinate of this chunk.
     * @return The Z coordinate of this chunk.
     */
	public int getZ() {
		return z;
	}

    /**
     * Gets the type of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The type.
     */
	public int getType(int x, int z, int y) {
		return types[coordToIndex(x, z, y)];
	}

    /**
     * Sets the type of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param type The type.
     */
	public void setType(int x, int z, int y, int type) {
		if (type < 0 || type >= 16)
			throw new IllegalArgumentException();

		types[coordToIndex(x, z, y)] = (byte) type;
	}

    /**
     * Gets the metadata of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The metadata.
     */
	public int getMetaData(int x, int z, int y) {
		return metaData[coordToIndex(x, z, y)];
	}

    /**
     * Sets the metadata of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param metaData The metadata.
     */
	public void setMetaData(int x, int z, int y, int metaData) {
		if (metaData < 0 || metaData >= 16)
			throw new IllegalArgumentException();

		this.metaData[coordToIndex(x, z, y)] = (byte) metaData;
	}

    /**
     * Gets the sky light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The sky light level.
     */
	public int getSkyLight(int x, int z, int y) {
		return skyLight[coordToIndex(x, z, y)];
	}

    /**
     * Sets the sky light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param skyLight The sky light level.
     */
	public void setSkyLight(int x, int z, int y, int skyLight) {
		if (skyLight < 0 || skyLight >= 16)
			throw new IllegalArgumentException();

		this.skyLight[coordToIndex(x, z, y)] = (byte) skyLight;
	}

    /**
     * Gets the block light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The block light level.
     */
	public int getBlockLight(int x, int z, int y) {
		return blockLight[coordToIndex(x, z, y)];
	}

    /**
     * Sets the block light level of a block within this chunk.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @param blockLight The block light level.
     */
	public void setBlockLight(int x, int z, int y, int blockLight) {
		if (blockLight < 0 || blockLight >= 16)
			throw new IllegalArgumentException();

		this.blockLight[coordToIndex(x, z, y)] = (byte) blockLight;
	}

    /**
     * Creates a new {@link Message} which can be sent to a client to stream
     * this chunk to them.
     * @return The {@link CompressedChunkMessage}.
     */
	public Message toMessage() {
		return new CompressedChunkMessage(x * Chunk.WIDTH, z * Chunk.HEIGHT, 0, WIDTH, HEIGHT, DEPTH, serializeTileData());
	}

    /**
     * Converts a three-dimensional coordinate to an index within the
     * one-dimensional arrays.
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The index within the arrays.
     */
	private int coordToIndex(int x, int z, int y) {
		if (x < 0 || z < 0 || y < 0 || x >= WIDTH || z >= HEIGHT || y >= DEPTH)
			throw new IndexOutOfBoundsException();

		return (x * HEIGHT + z) * DEPTH + y;
	}

    /**
     * Serializes tile data into a byte array.
     * @return The byte array populated with the tile data.
     */
	private byte[] serializeTileData() {
		byte[] dest = new byte[((WIDTH * HEIGHT * DEPTH * 5) / 2)];

		System.arraycopy(types, 0, dest, 0, types.length);

		int pos = types.length;

		for (int i = 0; i < (metaData.length / 2); i++) {
			byte meta1 = metaData[i];
			byte meta2 = metaData[i + 1];
			dest[pos++] = (byte) ((meta1 << 4) | meta2);
		}

		for (int i = 0; i < (skyLight.length / 2); i++) {
			byte light1 = skyLight[i];
			byte light2 = skyLight[i + 1];
			dest[pos++] = (byte) ((light1 << 4) | light2);
		}

		for (int i = 0; i < (blockLight.length / 2); i++) {
			byte light1 = blockLight[i];
			byte light2 = blockLight[i + 1];
			dest[pos++] = (byte) ((light1 << 4) | light2);
		}

		return dest;
	}

}
