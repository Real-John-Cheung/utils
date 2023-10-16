
/*
Parameters for recording the sketch
*/
boolean isRendering = false;
// using the r key to start and stop the recording
// change to other keys if conflict with your interation setting
char toggleRecordingKey = 'r'; 

void setup(){
  // put your setup code here
  
}

void draw(){
  // put your code here to loop
  
  // keep the below code at the bottom of the draw function
  if (isRendering) render();
}

/*
Function for rendering your sketch
this function will render each frame of your running sketch into a png image file
please visit the following website to learn about how to turn the rendered 
images into a video or animated gif with the "Movie Maker" tool of Processing:
http://real-john-cheung.github.io/utils/processingRecordingTmpl/Instruction.pdf
By default it saves the images into a folder call "render"
*/ 
void render(){
  saveFrame("render/######.png");
}

void keyReleased(){
  if (key == toggleRecordingKey) isRendering = !isRendering;
}
