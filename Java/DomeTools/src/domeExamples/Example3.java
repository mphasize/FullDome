/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domeExamples;

import processing.core.PApplet;
import processing.core.PGraphics;

/**
 *
 * @author mphasize
 */
public class Example3 extends DomeSketchImp {

	int numBalls = 12;
	float spring = 0.05f;
	float gravity = 0.03f;
	float friction = -0.9f;
	Ball[] balls = new Ball[numBalls];

	public void setup() {
		p5.noStroke();
		p5.smooth();
		for (int i = 0; i < numBalls; i++) {
			balls[i] = new Ball((float) Math.random() * p5.width, (float) Math.random() * p5.height, ((float) Math.random() * 20) + 20, i, balls);
		}

	}

	public void draw() {
		p5.beginDraw();
		p5.background(0);
		for (int i = 0; i < numBalls; i++) {
			balls[i].collide();
			balls[i].move();
			balls[i].display();
		}
		p5.endDraw();

	}

	public Example3(PGraphics g) {
		super(g);
	}

	class Ball {

		float x, y;
		float diameter;
		float vx = 0;
		float vy = 0;
		int id;
		Ball[] others;

		Ball(float xin, float yin, float din, int idin, Ball[] oin) {
			x = xin;
			y = yin;
			diameter = din;
			id = idin;
			others = oin;
		}

		void collide() {
			for (int i = id + 1; i < numBalls; i++) {
				float dx = others[i].x - x;
				float dy = others[i].y - y;
				float distance = PApplet.sqrt(dx * dx + dy * dy);
				float minDist = others[i].diameter / 2 + diameter / 2;
				if (distance < minDist) {
					float angle = PApplet.atan2(dy, dx);
					float targetX = x + PApplet.cos(angle) * minDist;
					float targetY = y + PApplet.sin(angle) * minDist;
					float ax = (targetX - others[i].x) * spring;
					float ay = (targetY - others[i].y) * spring;
					vx -= ax;
					vy -= ay;
					others[i].vx += ax;
					others[i].vy += ay;
				}
			}
		}

		void move() {
			vy += gravity;
			x += vx;
			y += vy;
			if (x + diameter / 2 > p5.width) {
				x = p5.width - diameter / 2;
				vx *= friction;
			} else if (x - diameter / 2 < 0) {
				x = diameter / 2;
				vx *= friction;
			}
			if (y + diameter / 2 > p5.height) {
				y = p5.height - diameter / 2;
				vy *= friction;
			} else if (y - diameter / 2 < 0) {
				y = diameter / 2;
				vy *= friction;
			}
		}

		void display() {
			p5.fill(255, 204);
			p5.ellipse(x, y, diameter, diameter);
		}
	}
}
