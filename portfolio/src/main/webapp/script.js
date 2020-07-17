const facts = [
  'i love cats', 
  'i love programming', 
  'i love sundays', 
  'i love cakes'
]

function getRandomFact() {
  const fact = facts[Math.floor(Math.random() * facts.length)];
  const factContainer = document.getElementById('fact-container');
  factContainer.innerText = fact;
}
