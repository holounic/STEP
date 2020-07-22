const facts = [
  'i love cats',
  'i love programming',
  'i love sundays',
  'i love cakes',
]

function getRandomFact() {
  const fact = facts[Math.floor(Math.random() * facts.length)];
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}

function getComment() {
  fetch('/data')
  .then(response => response.json())
  .then(comments => {
    const commentContainer = document.getElementById('comment-container');
    let index = 0;
    let ID = 'comment-container';

    for (let comment of comments) {
      const currentId = ID + '-' + index;
      const section = '<div id=\"'+ currentId + '\"></div>';
      commentContainer.innerHTML += section;
      $('#' + currentId).text(comment.text);
      index += 1;
    }
  });
}