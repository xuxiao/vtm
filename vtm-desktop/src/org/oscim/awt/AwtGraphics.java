/*
 * Copyright 2013 Hannes Janetzek
 *
 * This file is part of the OpenScienceMap project (http://www.opensciencemap.org).
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.oscim.awt;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import org.oscim.backend.CanvasAdapter;
import org.oscim.backend.canvas.Bitmap;
import org.oscim.backend.canvas.Canvas;
import org.oscim.backend.canvas.Paint;

public class AwtGraphics extends CanvasAdapter {

	public static void init() {
		CanvasAdapter.init(new AwtGraphics());
	}

	private AwtGraphics() {
		// do nothing
	}

	@Override
	public Paint newPaintImpl() {
		return new AwtPaint();
	}

	@Override
	public Bitmap newBitmapImpl(int width, int height, int format) {
		return new AwtBitmap(width, height, format);
	}

	@Override
	public Canvas newCanvasImpl() {
		return new AwtCanvas();
	}

	static final BufferedImage image;

	static final Graphics2D canvas;

	static {
		image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		canvas = image.createGraphics();
		canvas.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
		                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//canvas.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		//canvas.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	}

	static synchronized FontMetrics getFontMetrics(Font font) {
		canvas.setFont(font);
		// get character measurements
		FontMetrics fm = canvas.getFontMetrics();
		// int ascent = fm.getMaxAscent();
		// int descent = fm.getMaxDescent();
		// int advance = fm.charWidth('W'); // width of widest char, more
		// reliable than getMaxAdvance();
		// int leading = fm.getLeading();
		//
		return fm;
	}

	static synchronized float getTextWidth(FontMetrics fm, String text) {
		//return (float)fm.getStringBounds(text, canvas).getWidth();
		return fm.stringWidth(text);
	}

	@Override
	public Bitmap decodeBitmapImpl(InputStream inputStream) {
		try {
			return new AwtBitmap(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Bitmap loadBitmapAssetImpl(String fileName) {
		try {
			return createBitmap(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
