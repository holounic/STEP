function createImage(name) {
  const link = `images/${name}.jpg`;
  return `<img src=\"${link}\" alt=\"${name}\"></img>`
}

const images = [
  createImage('beach'),
  createImage('cat'), 
  createImage('dog'), 
  createImage('door'), 
  createImage('forest'), 
  createImage('fruit'), 
  createImage('grass'),
  createImage('house'),
  createImage('plain'),
  createImage('red_cat'), 
  createImage('river'),
  createImage('sea'),  
  createImage('ship'),
  createImage('street'),
  createImage('watermelon'),
]

function getPhotos() {
  const photoContainer = document.getElementById('photo-container');
  for (let image of images) {
    const imgContainer = `<div class=\"col-2\">${image}</div>`;
    photoContainer.innerHTML += imgContainer;
  }
}