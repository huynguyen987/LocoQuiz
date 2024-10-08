// File: js/quiz-creator.js

document.addEventListener('DOMContentLoaded', function () {
    let questionCount = 0;
    let quizType = 'multiple-choice';

    const questionCountInput = document.getElementById('questionCount');
    const quizTypeInput = document.getElementById('quizType');
    const questionTableBody = document.getElementById('questionTableBody');
    const questionsContainer = document.getElementById('questionsContainer');
    const addQuestionBtn = document.getElementById('addQuestionBtn');
    const quizTypeButtons = document.querySelectorAll('.quiz-type-btn');

    // Dropdown Elements
    const dropdown = document.querySelector('.dropdown');
    const dropdownToggle = document.querySelector('.dropdown-toggle');
    const dropdownMenu = document.querySelector('.dropdown-menu');

    // Function to toggle dropdown
    function toggleDropdown() {
        dropdown.classList.toggle('open');
        dropdownToggle.setAttribute('aria-expanded', dropdown.classList.contains('open'));
    }

    // Close dropdown when clicking outside
    function closeDropdown(event) {
        if (!dropdown.contains(event.target)) {
            dropdown.classList.remove('open');
            dropdownToggle.setAttribute('aria-expanded', 'false');
        }
    }

    // Event listener for dropdown toggle
    dropdownToggle.addEventListener('click', function (event) {
        event.stopPropagation(); // Prevent event from bubbling up to document
        toggleDropdown();
    });

    // Event listener for clicking inside dropdown menu
    dropdownMenu.addEventListener('click', function (event) {
        event.stopPropagation(); // Prevent event from bubbling up to document
    });

    // Event listener for selecting/deselecting tags
    dropdownMenu.addEventListener('change', function () {
        updateDropdownLabel();
    });

    // Update the dropdown toggle button text based on selected tags
    function updateDropdownLabel() {
        const selectedTags = Array.from(dropdownMenu.querySelectorAll('input[type="checkbox"]:checked'))
            .map(checkbox => checkbox.parentElement.textContent.trim());
        if (selectedTags.length > 0) {
            dropdownToggle.textContent = selectedTags.join(', ');
            dropdownToggle.appendChild(createChevronIcon());
        } else {
            dropdownToggle.textContent = 'Select Tags';
            dropdownToggle.appendChild(createChevronIcon());
        }
    }

    // Function to create chevron icon
    function createChevronIcon() {
        const chevron = document.createElement('i');
        chevron.classList.add('fas', 'fa-chevron-down');
        return chevron;
    }

    // Close dropdown when clicking outside
    document.addEventListener('click', closeDropdown);

    // Function to switch quiz type
    function switchQuizType(type) {
        quizType = type;
        quizTypeInput.value = type;
        questionCount = 0;
        questionCountInput.value = questionCount;
        questionTableBody.innerHTML = '';
        questionsContainer.innerHTML = '';
        // Update active button
        quizTypeButtons.forEach(btn => {
            const isActive = btn.getAttribute('data-type') === type;
            btn.classList.toggle('active', isActive);
            btn.setAttribute('aria-pressed', isActive);
        });
    }

    // Event listeners for quiz type buttons
    quizTypeButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            switchQuizType(btn.getAttribute('data-type'));
        });
    });

    // Function to add a question based on the quiz type
    function addQuestion() {
        questionCount++;
        questionCountInput.value = questionCount;

        // Add row to the question table
        const row = document.createElement('tr');
        row.id = `row${questionCount}`;
        row.innerHTML = `
          <td><a href="#question${questionCount}">Question ${questionCount}</a></td>
          <td><button type="button" class="remove-btn" data-num="${questionCount}">Remove</button></td>
        `;
        questionTableBody.appendChild(row);

        // Add question section based on the quiz type
        let questionHTML = '';

        if (quizType === 'multiple-choice') {
            questionHTML = getMultipleChoiceQuestionHTML(questionCount);
        } else if (quizType === 'fill-in-the-blank') {
            questionHTML = getFillInTheBlankQuestionHTML(questionCount);
        } else if (quizType === 'matching') {
            questionHTML = getMatchingQuestionHTML(questionCount);
        }

        const questionSection = document.createElement('div');
        questionSection.classList.add('question-section');
        questionSection.id = `question${questionCount}`;
        questionSection.innerHTML = questionHTML;
        questionsContainer.appendChild(questionSection);
    }

    // Function to remove a question
    function removeQuestion(num) {
        // Remove the table row and question section
        const row = document.getElementById(`row${num}`);
        const questionSection = document.getElementById(`question${num}`);
        if (row) row.remove();
        if (questionSection) questionSection.remove();

        // Decrease questionCount and update the hidden field
        questionCount--;
        questionCountInput.value = questionCount;

        // Update IDs and numbers of subsequent questions
        updateQuestionNumbers();
    }

    // Update question numbers after removal
    function updateQuestionNumbers() {
        const questionSections = document.querySelectorAll('.question-section');
        const tableRows = questionTableBody.querySelectorAll('tr');
        questionSections.forEach((section, index) => {
            const newNum = index + 1;
            section.id = `question${newNum}`;
            section.querySelector('h3').textContent = `Question ${newNum} (${formatQuizType(quizType)})`;

            // Update IDs and names within the question section
            updateQuestionFields(section, newNum);
        });
        tableRows.forEach((row, index) => {
            const newNum = index + 1;
            row.id = `row${newNum}`;
            row.querySelector('a').setAttribute('href', `#question${newNum}`);
            row.querySelector('a').textContent = `Question ${newNum}`;
            row.querySelector('.remove-btn').setAttribute('data-num', newNum);
        });
    }

    // Helper function to update question field IDs and names
    function updateQuestionFields(section, num) {
        const inputs = section.querySelectorAll('input, textarea');
        inputs.forEach(input => {
            const name = input.name.replace(/\d+/, num);
            const id = input.id.replace(/\d+/, num);
            input.name = name;
            input.id = id;
            if (input.type === 'radio' || input.type === 'checkbox') {
                input.value = input.value.replace(/\d+/, num);
            }
        });
        const labels = section.querySelectorAll('label');
        labels.forEach(label => {
            const htmlFor = label.getAttribute('for');
            if (htmlFor) {
                const newFor = htmlFor.replace(/\d+/, num);
                label.setAttribute('for', newFor);
            }
        });
    }

    // Helper function to format quiz type
    function formatQuizType(type) {
        switch (type) {
            case 'multiple-choice':
                return 'Multiple Choice';
            case 'fill-in-the-blank':
                return 'Fill in the Blank';
            case 'matching':
                return 'Matching';
            default:
                return '';
        }
    }

    // Helper functions to get HTML for each quiz type
    function getMultipleChoiceQuestionHTML(num) {
        return `
          <h3>Question ${num} (Multiple Choice)</h3>
          <!-- Question Content -->
          <label for="questionContent${num}">Question ${num}:</label>
          <textarea id="questionContent${num}" name="questionContent${num}" required aria-required="true"></textarea>

          <!-- Answers -->
          <label>Answers:</label>
          ${[1, 2, 3, 4].map(i => `
          <div class="answer-option">
            <input type="radio" id="correctAnswer${num}_${i}" name="correctAnswer${num}" value="${i}" required aria-required="true">
            <label for="answer${num}_${i}">Answer ${i}:</label>
            <input type="text" id="answer${num}_${i}" name="answer${num}_${i}" placeholder="Answer ${i}" required aria-required="true">
          </div>
          `).join('')}
        `;
    }

    function getFillInTheBlankQuestionHTML(num) {
        return `
          <h3>Question ${num} (Fill in the Blank)</h3>
          <!-- Question Content -->
          <label for="questionContent${num}">Question ${num}:</label>
          <textarea id="questionContent${num}" name="questionContent${num}" required aria-required="true"></textarea>
          <!-- Correct Answer -->
          <label for="correctAnswer${num}">Correct Answer:</label>
          <input type="text" id="correctAnswer${num}" name="correctAnswer${num}" required aria-required="true">
        `;
    }

    function getMatchingQuestionHTML(num) {
        return `
          <h3>Question ${num} (Matching)</h3>
          <!-- Instructions -->
          <p>Enter the items for Column A and Column B. The answers will be matched in the order you enter them.</p>
          <div id="matchingPairs${num}">
            ${getMatchingPairHTML(num, 1)}
          </div>
          <button type="button" class="add-pair-btn" data-question="${num}">Add Matching Pair</button>
        `;
    }

    function getMatchingPairHTML(questionNum, pairNum) {
        return `
          <div class="matching-pair">
            <label for="matchA${questionNum}_${pairNum}">Column A Item ${pairNum}:</label>
            <input type="text" id="matchA${questionNum}_${pairNum}" name="matchA${questionNum}_${pairNum}" required aria-required="true">
            <label for="matchB${questionNum}_${pairNum}">Column B Item ${pairNum}:</label>
            <input type="text" id="matchB${questionNum}_${pairNum}" name="matchB${questionNum}_${pairNum}" required aria-required="true">
          </div>
        `;
    }

    // Event delegation for removing questions
    questionTableBody.addEventListener('click', function (e) {
        if (e.target && e.target.classList.contains('remove-btn')) {
            const num = e.target.getAttribute('data-num');
            removeQuestion(num);
        }
    });

    // Event delegation for adding matching pairs
    questionsContainer.addEventListener('click', function (e) {
        if (e.target && e.target.classList.contains('add-pair-btn')) {
            const questionNum = e.target.getAttribute('data-question');
            const matchingPairsContainer = document.getElementById(`matchingPairs${questionNum}`);
            const currentPairs = matchingPairsContainer.querySelectorAll('.matching-pair').length;
            const newPairNum = currentPairs + 1;
            matchingPairsContainer.insertAdjacentHTML('beforeend', getMatchingPairHTML(questionNum, newPairNum));
        }
    });

    // Add question button click event
    addQuestionBtn.addEventListener('click', addQuestion);

    // Initialize with default quiz type
    switchQuizType('multiple-choice');

    // Function to update dropdown label on page load
    updateDropdownLabel();
});
