# 💰 Midnight Thief: Tile-Based Stealth Game

**Midnight Thief** es un videojuego de sigilo y estrategia desarrollado en **Java** utilizando la librería **Processing**. El jugador asume el papel de un maestro ladrón que debe infiltrarse en una bóveda de alta seguridad, evitar a los guardias patrulleros y escapar con el botín.

---

## 🚀 Desafíos Técnicos Superados

Este proyecto no es solo un juego de movimiento; implementa conceptos fundamentales de la ingeniería de software y el desarrollo de videojuegos:

* **Renderizado por Tiles (Baldosas):** El nivel se genera dinámicamente procesando una matriz de strings (`String[]`), lo que permite un diseño de niveles modular y eficiente.
* **AABB Collision Detection:** Implementación del algoritmo *Axis-Aligned Bounding Box* para gestionar colisiones precisas entre el jugador y los guardias, garantizando que el juego detecte el contacto incluso a diferentes velocidades.
* **Algoritmo de "Corner Cutting" (Ajuste de Esquinas):** Para mejorar la experiencia de usuario (UX), desarrollé una lógica basada en **aritmética modular** que alinea automáticamente al personaje con los pasillos si el giro del jugador tiene un ligero margen de error.
* **IA de Patrullaje Aleatorio:** Los enemigos utilizan un sistema de toma de decisiones basado en probabilidades y detección de colisiones con el entorno para patrullar el mapa de forma impredecible.
* **Gestión de Estados de Juego:** Control de flujo mediante banderas lógicas para transiciones suaves entre el estado de juego, Game Over y Victoria.

---

## 🛠️ Tecnologías y Herramientas

* **Lenguaje:** Java 8
* **Framework Gráfico:** Processing Core Library
* **Entorno de Desarrollo:** NetBeans IDE
* **Arquitectura:** Programación Estructurada con Arreglos Paralelos

---

## 🎮 Instrucciones de Juego

### Objetivo
Llega al **Tesoro (T)** esquivando a los **Guardias (G/V)**. Si un guardia te toca, serás arrestado.

### Controles
* **Moverse:** `W-A-S-D` o `Flechas de Dirección`.
* **Reiniciar:** Pulsa `R` en cualquier momento o tras ser capturado.

---

## 📁 Estructura del Proyecto

* `/src`: Contiene el código fuente principal (`Game2D.java`).
* `/img`: Assets visuales (Ladrón, Guardias, Muros y Tesoro).
* `/lib`: Librerías externas necesarias (core.jar de Processing).

---

## 👷 Autor
**Karyoli Nieves** 

## 📬 Contact

Feel free to reach out to me for collaboration or opportunities!

*   **Email:** karyoli.ie@gmail.com
*   **LinkedIn:** [Karyoli Nieves](https://www.linkedin.com/in/karyoli-nieves/)
*   **GitHub:** [@nkaryoli](https://github.com/nkaryoli)
*   **Portfolio:** [Karyoli Nieves](https://nkaryoli.github.io/miPortfolio/)

