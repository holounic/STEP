
/**
 * Adds a fetched greeting to the page.
 */
function addGreeting() {
    fetch('\data')
    .then(response => response.text())
    .then((greeting) => document.getElementById('greeting-container').innerHTML = greeting);
}
