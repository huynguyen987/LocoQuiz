// Sample Correct Answers
const correctAnswers = {
    "1": "Ottawa",
    "2": "Berlin",
    "3": "Tokyo",
    "4": "Canberra",
    "5": "Brasília"
    // Add more correct answers as needed
};

// Object to keep track of user matches
let userMatches = {};

// Function to Shuffle Array Elements
function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

// Initialize Interact.js for Drag and Drop
function initializeDragAndDrop() {
    interact('.draggable').draggable({
        inertia: true,
        autoScroll: true,
        listeners: {
            move (event) {
                const target = event.target;
                // Keep the dragged position in the data-x/data-y attributes
                const x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx;
                const y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

                // Translate the element
                target.style.transform = `translate(${x}px, ${y}px)`;

                // Update the position attributes
                target.setAttribute('data-x', x);
                target.setAttribute('data-y', y);
            },
            end (event) {
                const target = event.target;
                const dropzoneId = target.getAttribute('data-dropzone');

                if (!dropzoneId) {
                    // Snap back to original position
                    target.style.transform = 'translate(0px, 0px)';
                    target.removeAttribute('data-x');
                    target.removeAttribute('data-y');
                }
            }
        }
    });

    interact('.dropzone').dropzone({
        accept: '.draggable',
        overlap: 0.75,

        ondropactivate: function (event) {
            event.target.classList.add('drop-active');
        },
        ondragenter: function (event) {
            const draggableElement = event.relatedTarget;
            const dropzoneElement = event.target;

            dropzoneElement.classList.add('over');
            draggableElement.classList.add('can-drop');
        },
        ondragleave: function (event) {
            event.target.classList.remove('over');
            event.relatedTarget.classList.remove('can-drop');
        },
        ondrop: function (event) {
            const draggableElement = event.relatedTarget;
            const dropzoneElement = event.target;

            // Check if dropzone already has a draggable
            if (dropzoneElement.querySelector('.draggable')) {
                alert('This capital already has a country matched to it.');
                return;
            }

            // Get IDs
            const draggableId = draggableElement.getAttribute('data-id');
            const dropzoneId = dropzoneElement.getAttribute('data-id');

            // Assign match
            userMatches[draggableId] = dropzoneElement.textContent.trim();

            // Snap the draggable to the dropzone
            draggableElement.style.transform = 'translate(0px, 0px)';
            draggableElement.removeAttribute('data-x');
            draggableElement.removeAttribute('data-y');
            draggableElement.setAttribute('data-dropzone', dropzoneId);

            // Append the draggable to the dropzone
            dropzoneElement.appendChild(draggableElement);

            // Update Progress Bar
            updateProgress();
        },
        ondropdeactivate: function (event) {
            event.target.classList.remove('drop-active');
            event.target.classList.remove('over');
        }
    });
}

// Function to Check Answers
function checkAnswers() {
    let score = 0;
    let total = Object.keys(correctAnswers).length;
    let mismatches = [];

    for (let id in correctAnswers) {
        if (userMatches[id] === correctAnswers[id]) {
            score++;
            highlightCorrect(id, true);
        } else {
            mismatches.push(id);
            highlightCorrect(id, false);
        }
    }

    // Display Result
    let resultModal = document.getElementById('result-modal');
    let resultText = document.getElementById('result-text');
    resultText.textContent = `You scored ${score} out of ${total}.`;
    resultModal.style.display = 'block';
}

// Function to Highlight Correct and Incorrect Matches
function highlightCorrect(id, isCorrect) {
    const draggable = document.querySelector(`.draggable[data-id='${id}']`);
    if (isCorrect) {
        draggable.classList.add('correct');
        draggable.classList.remove('incorrect');
    } else {
        draggable.classList.add('incorrect');
        draggable.classList.remove('correct');
    }
}

// Function to Close Modal
function closeModal() {
    let resultModal = document.getElementById('result-modal');
    resultModal.style.display = 'none';
}

// Function to Reset Quiz
function resetQuiz() {
    userMatches = {};
    let draggables = document.querySelectorAll('.draggable');
    draggables.forEach(draggable => {
        draggable.classList.remove('correct', 'incorrect');
        draggable.style.transform = 'translate(0px, 0px)';
        draggable.removeAttribute('data-dropzone');
        document.getElementById('countries').appendChild(draggable);
    });

    let dropzones = document.querySelectorAll('.dropzone');
    dropzones.forEach(dropzone => {
        // Remove any draggables inside dropzones
        let children = dropzone.querySelectorAll('.draggable');
        children.forEach(child => {
            dropzone.removeChild(child);
        });
    });

    // Shuffle again
    let countries = document.getElementById('countries');
    let capitals = document.getElementById('capitals');

    let draggableElements = Array.from(countries.children);
    let dropzoneElements = Array.from(capitals.children);

    shuffleArray(draggableElements);
    shuffleArray(dropzoneElements);

    // Append shuffled elements back to their containers
    draggableElements.forEach(el => countries.appendChild(el));
    dropzoneElements.forEach(el => capitals.appendChild(el));

    // Re-initialize drag and drop
    initializeDragAndDrop();

    // Reset Progress Bar
    resetProgress();

    // Close Modal
    let resultModal = document.getElementById('result-modal');
    resultModal.style.display = 'none';
}

// Function to Update Progress Bar
function updateProgress() {
    const total = Object.keys(correctAnswers).length;
    const matched = Object.keys(userMatches).length;
    const progressBar = document.getElementById('progress-bar');
    const percentage = (matched / total) * 100;
    progressBar.style.width = `${percentage}%`;
}

// Function to Reset Progress Bar
function resetProgress() {
    const progressBar = document.getElementById('progress-bar');
    progressBar.style.width = `0%`;
}

// Close modal when clicking outside of it
window.onclick = function(event) {
    let resultModal = document.getElementById('result-modal');
    if (event.target == resultModal) {
        resultModal.style.display = "none";
    }
}

// Initialize the Quiz on Page Load
window.onload = function() {
    let savedMatches = JSON.parse(localStorage.getItem('userMatches'));
    if (savedMatches) {
        userMatches = savedMatches;
        // Assign matches and update the UI accordingly
        for (let id in userMatches) {
            let capital = userMatches[id];
            let dropzone = document.querySelector(`.dropzone[data-id='${id}']`);
            let draggable = document.querySelector(`.draggable[data-id='${id}']`);
            if (draggable && dropzone) {
                dropzone.appendChild(draggable);
                draggable.setAttribute('data-dropzone', id);
                draggable.style.transform = 'translate(0px, 0px)';
                highlightCorrect(id, draggable.textContent.trim() === correctAnswers[id]);
            }
        }
        updateProgress();
    }

    // Shuffle items if desired
    let countries = document.getElementById('countries');
    let capitals = document.getElementById('capitals');

    let draggableElements = Array.from(countries.children);
    let dropzoneElements = Array.from(capitals.children);

    shuffleArray(draggableElements);
    shuffleArray(dropzoneElements);

    // Append shuffled elements back to their containers
    draggableElements.forEach(el => countries.appendChild(el));
    dropzoneElements.forEach(el => capitals.appendChild(el));

    // Initialize drag and drop
    initializeDragAndDrop();
};
