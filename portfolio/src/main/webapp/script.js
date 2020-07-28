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

function getRandomFact() {
  const fact = facts[Math.floor(Math.random() * facts.length)];
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

function getComments() {
  fetch('/data')
    .then(response => response.json())
    .then(comments => {
      const commentContainer = document.getElementById('comment-container');
      let index = 0;
      let ID = 'comment-container';

      for (let comment of comments) {
        const currentId = ID + '-' + index;
        const section = '<div id=\"' + currentId + '\" class="comment"></div>';
        commentContainer.innerHTML += section;
        $('#' + currentId).text(comment.text);
        index += 1;
      }
    });
}

function deleteComments() {
  const request = new Request('/delete-data', {
    method: 'POST'
  });
  fetch(request)
    .then(() => {
      document.getElementById('comment-container').innerHTML = "";
    })
}

function getPhotos() {
  const photoContainer = document.getElementById('photo-container');
  for (let image of images) {
    const imgContainer = `<div class=\"col-2\">${image}</div>`;
    photoContainer.innerHTML += imgContainer;
  }
}