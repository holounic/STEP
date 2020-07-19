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

function getGreeting() {
  fetch('/data')
  .then(response => response.text())
  .then((greeting) => {
    const greetingContainer = document.getElementById('greeting-container');
    greetingContainer.innerHTML = greeting;
  })
}