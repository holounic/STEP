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
  console.log(10);
  fetch('/data')
  .then(response => response.json())
  .then((comments) => {
    const commentContainer = document.getElementById('comment-container');
    let styledComments = "";
    for (let i = 0; i < comments.length; i++) {
      styledComments += ('<p>' + comments[i].number + '</p>');
    }
    commentContainer.innerHTML = styledComments;
  });
}