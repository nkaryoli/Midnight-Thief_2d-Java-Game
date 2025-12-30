package game2d;

import processing.core.PApplet;
import processing.core.PImage;

public class Game2D extends PApplet {

   PImage imgWall, imgGuard, imgSpy, imgTile, imgPrice;

   int rowCount = 19;
   int columnCount = 19;
   int tileSize = 32;
   int boardWidth = columnCount * tileSize;
   int boardHeight = rowCount * tileSize;

   // ------ TABLERO -----
   // X = pared, G = guardia, T = Tesoro, S = espia
   String[] tileMap = {
      "XXXXXXXXXXXXXXXXXXX", // 0
      "X        X        X", // 1
      "X XX XXX X XXX XX X", // 2
      "X X      G      X X", // 3 (Guardia patrullando arriba)
      "X X XX XXXXX XX X X", // 4
      "X    X       X    X", // 5
      "XXXX XXX X XXX XXXX", // 6
      "X      X X        X", // 7
      "X XXXXXX XXXX XXX X", // 8
      "X X    X TX     X X", // 9 (EL TESORO EN LA BÓVEDA)
      "X XXXX XXXXXXXX X X", // 10
      "X        X        X", // 11
      "XXXX XXX X XXX XXXX", // 12
      "X    X       X    X", // 13
      "X XX X XXXXX X XX X", // 14
      "X X      V      X X", // 15 (Guardia patrullando abajo)
      "X X XXXX X XXXX X X", // 16
      "X        S        X", // 17 (INICIO DEL JUGADOR)
      "XXXXXXXXXXXXXXXXXXX" // 18
   };

   public static void main(String[] args) {
      PApplet.main("game2d.Game2D");
   }

   int spyX;
   int spyY;
   int spyStartX;
   int spyStartY;

   // Definimos cuántos guardias queremos (puedes poner 2 o los que quieras)
   int numGuards = 2;
   int[] guardsX = new int[numGuards];
   int[] guardsY = new int[numGuards];
   char[] guardsDirections = new char[numGuards];
   int[] guardsStartX = new int[numGuards];
   int[] guardsStartY = new int[numGuards];

// Necesitaremos un contador para saber cuántos hemos encontrado en el mapa
   int guardsFound = 0;

   char[] directions = {'U', 'R', 'D', 'L'};

   char spyDirection;
   int spyVelocity = 3;
   int guardsVelocity = spyVelocity - 1;

   boolean isMoving = false;
   boolean isGameOver = false;

   @Override
   public void settings() {
      size(boardWidth, boardHeight); // Esto define el tamaño de la ventana
   }

   @Override
   public void setup() {
      // cargar imagenes
      imgWall = loadImage("./img/wall.png");
      imgSpy = loadImage("./img/spy.png");
      imgGuard = loadImage("./img/guard.png");
      imgTile = loadImage("./img/tile.png");
      imgPrice = loadImage("./img/price.png");

      imgWall.resize(tileSize, tileSize);
      imgSpy.resize(tileSize, tileSize);
      imgGuard.resize(tileSize, tileSize);
      imgPrice.resize(tileSize, tileSize);
      imgTile.resize(tileSize, tileSize);

      background(0, 0, 0);
      spyStartX = 9 * tileSize;
      spyStartY = 17 * tileSize;

      for (int r = 0; r < rowCount; r++) {
         for (int c = 0; c < columnCount; c++) {
            String row = tileMap[r];
            char tileMapChar = row.charAt(c);

            if (tileMapChar == 'G' || tileMapChar == 'V') {
               if (guardsFound < numGuards) {
                  guardsX[guardsFound] = c * tileSize;
                  guardsStartX[guardsFound] = c * tileSize;
                  guardsY[guardsFound] = r * tileSize;
                  guardsStartY[guardsFound] = r * tileSize;
                  guardsDirections[guardsFound] = directions[(int) random(4)];
                  guardsFound++; // Pasamos al siguiente espacio del array
               }

            } else if (tileMapChar == 'S') {
               spyX = c * tileSize;
               spyY = r * tileSize;
            }
         }
      }

   }

   @Override
   public void draw() {
      if (!isGameOver) {
         loadMap();
         move();
         chechGuardCollisions();
         checkVictory();

         image(imgSpy, spyX, spyY);

         for (int i = 0; i < guardsFound; i++) {
            image(imgGuard, guardsX[i], guardsY[i]);
         }

         chechGuardCollisions();
      } else {
         int col = (spyX + tileSize / 2) / tileSize;
         int row = (spyY + tileSize / 2) / tileSize;

         if (tileMap[row].charAt(col) == 'T') {
            showVictoryScreen();
         } else {
            showGameOverScreen();
         }
      }

   }

   // --- CONTROL DE TECLADO (EVENTOS) ---
   @Override
   public void keyPressed() {
      isMoving = true;
      if (keyCode == UP || key == 'w') {
         spyDirection = 'U';
      }
      if (keyCode == DOWN || key == 's') {
         spyDirection = 'D';
      }
      if (keyCode == LEFT || key == 'a') {
         spyDirection = 'L';
      }
      if (keyCode == RIGHT || key == 'd') {
         spyDirection = 'R';
      }

      // Reiniciar juego con 'r'
      if (key == 'r' || key == 'R') {
         resetGame();
      }
   }

   @Override
   public void keyReleased() {
      isMoving = false;
   }

   public void loadMap() {
      for (int r = 0; r < rowCount; r++) {
         for (int c = 0; c < columnCount; c++) {
            String row = tileMap[r];
            char tileMapChar = row.charAt(c);

            // POSICION DE LOS ASSETS
            int x = c * tileSize;
            int y = r * tileSize;

            if (tileMapChar == 'X') { // pared
               image(imgWall, x, y);
            } else if (tileMapChar == 'T') { // tesoro
               image(imgTile, x, y);
               image(imgPrice, x, y);
            } else {
               image(imgTile, x, y);
            }
         }
      }
   }

   public void move() {
      if (isMoving) {
         // --- AJUSTE AUTOMÁTICO (CORNER CUTTING) ---
         adjustCorners();
         // --- MOVIMIENTO Y COLISIONES  ---
         // 1. Aplicamos el movimiento
         if (spyDirection == 'U') {
            spyY -= spyVelocity;
         } else if (spyDirection == 'D') {
            spyY += spyVelocity;
         } else if (spyDirection == 'L') {
            spyX -= spyVelocity;
         } else if (spyDirection == 'R') {
            spyX += spyVelocity;
         }

         // 2. Comprobamos colisiones con las esquinas (usando la lógica de 31px)
         // 3. Si hay colision, revertimos el movimiento/posicion anterior
         if (checkWallCollisions(spyX, spyY, spyDirection)) {
            if (spyDirection == 'U') {
               spyY += spyVelocity;
            } else if (spyDirection == 'D') {
               spyY -= spyVelocity;
            } else if (spyDirection == 'L') {
               spyX += spyVelocity;
            } else if (spyDirection == 'R') {
               spyX -= spyVelocity;
            }
         }

      }

      guardsMove();

   }

   public void adjustCorners() {
      int margin = 8; // pixeles de perdon

      if (spyDirection == 'U' || spyDirection == 'D') {
         // Corrijo la posicion horizontal (X)
         int errorX = spyX % tileSize;
         if (errorX > 0 && errorX <= margin) {
            spyX -= errorX; // Te empuja a la izquierda para centrarte
         } else if (errorX >= tileSize - margin) {
            spyX += (tileSize - errorX); // Te empuja a la derecha para centrarte
         }
      } else if (spyDirection == 'L' || spyDirection == 'R') {
         // corregimos la vertical (Y)
         int errorY = spyY % tileSize;
         if (errorY > 0 && errorY <= margin) {
            spyY -= errorY; // Te empuja hacia arriba
         } else if (errorY >= tileSize - margin) {
            spyY += (tileSize - errorY); // Te empuja hacia abajo
         }
      }
   }

   public boolean checkWallCollisions(int positionX, int positionY, char direction) {

      int left = positionX / tileSize;
      int right = (positionX + tileSize - 1) / tileSize;
      int up = positionY / tileSize;
      int down = (positionY + tileSize - 1) / tileSize;

      if (direction == 'U') {
         // Comprobamos las dos esquinas de arriba
         if (tileMap[up].charAt(left) == 'X' || tileMap[up].charAt(right) == 'X') {
            return true;
         }
      } else if (direction == 'D') {
         // Comprobamos las dos esquinas de abajo
         if (tileMap[down].charAt(left) == 'X' || tileMap[down].charAt(right) == 'X') {
            return true;
         }
      } else if (direction == 'L') {
         // Comprobamos las dos esquinas de la izquierda
         if (tileMap[up].charAt(left) == 'X' || tileMap[down].charAt(left) == 'X') {
            return true;
         }
      } else if (direction == 'R') {
         // Comprobamos las dos esquinas de la derecha
         if (tileMap[up].charAt(right) == 'X' || tileMap[down].charAt(right) == 'X') {
            return true;
         }
      }
      return false;
   }

   public void chechGuardCollisions() {
      for (int i = 0; i < guardsFound; i++) {
         int margen = 5;

         if (spyX < guardsX[i] + tileSize - margen
                 && spyX + tileSize > guardsX[i] + margen
                 && spyY < guardsY[i] + tileSize - margen
                 && spyY + tileSize > guardsY[i] + margen) {

            isGameOver = true;
         }
      }
   }

   public void guardsMove() {
      for (int i = 0; i < guardsFound; i++) {
         // 1. Intentar mover el guardia actual (el 'i')
         if (guardsDirections[i] == 'U') {
            guardsY[i] -= guardsVelocity;
         } else if (guardsDirections[i] == 'R') {
            guardsX[i] += guardsVelocity;
         } else if (guardsDirections[i] == 'D') {
            guardsY[i] += guardsVelocity;
         } else if (guardsDirections[i] == 'L') {
            guardsX[i] -= guardsVelocity;
         }

         // 2. Comprobar si ese guardia en concreto ha chocado
         if (checkWallCollisions(guardsX[i], guardsY[i], guardsDirections[i])) {
            // Revertir posición
            if (guardsDirections[i] == 'U') {
               guardsY[i] += guardsVelocity;
            } else if (guardsDirections[i] == 'R') {
               guardsX[i] -= guardsVelocity;
            } else if (guardsDirections[i] == 'D') {
               guardsY[i] -= guardsVelocity;
            } else if (guardsDirections[i] == 'L') {
               guardsX[i] += guardsVelocity;
            }

            // Cambiar dirección de ese guardia
            guardsDirections[i] = directions[(int) random(4)];
         }
      }
   }

   public void checkVictory() {
      // Calculamos qué baldosa está pisando el centro del espía
      int col = (spyX + tileSize / 2) / tileSize;
      int row = (spyY + tileSize / 2) / tileSize;

      // Si esa baldosa en el tileMap es una 'T'...
      if (tileMap[row].charAt(col) == 'T') {
         isGameOver = true;
         showVictoryScreen();
      }
   }

   public void showVictoryScreen() {
      background(35, 102, 30);
      textSize(50);
      textAlign(CENTER);
      fill(255, 215, 0);
      text("¡MISIÓN CUMPLIDA!", width / 2, height / 2);

      textSize(20);
      fill(255);
      text("Has recuperado el tesoro. Pulsa R para jugar de nuevo", width / 2, height / 2 + 50);
   }

   public void showGameOverScreen() {
      background(12, 12, 12);
      textSize(50);
      textAlign(CENTER);
      fill(255);
      text("¡GAME OVER!", width / 2, height / 2);
      textSize(20);
      text("Pulsa R para reiniciar", width / 2, height / 2 + 50);
   }

   public void resetGame() {
      spyX = spyStartX;
      spyY = spyStartY;

      spyVelocity = 3;
      guardsVelocity = 2;

      for (int i = 0; i < guardsFound; i++) {
         guardsX[i] = guardsStartX[i];
         guardsY[i] = guardsStartY[i];
      }

      isGameOver = false;

   }
}
