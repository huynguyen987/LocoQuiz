// referenceMaterials.js

// Khởi tạo danh sách tài liệu từ localStorage hoặc mảng trống
let materials = JSON.parse(localStorage.getItem('materials')) || [];

// Khởi tạo currentId từ localStorage hoặc đặt lại thành 1
let currentId = JSON.parse(localStorage.getItem('currentId')) || 1;

// Khởi tạo khi DOM đã được tải
document.addEventListener('DOMContentLoaded', function() {
    // Nếu chưa có dữ liệu, thêm một số mẫu dữ liệu
    if (materials.length === 0) {
        initializeSampleData();
    }
    displayMaterials();
    setupEventListeners();
});

// Thiết lập các sự kiện
function setupEventListeners() {
    const form = document.getElementById('materialForm');
    form.addEventListener('submit', addMaterial);

    document.getElementById('searchKeyword').addEventListener('input', filterMaterials);
    document.getElementById('filterType').addEventListener('change', filterMaterials);
    document.getElementById('clearFilters').addEventListener('click', clearFilters);
}

// Hàm thêm tài liệu mới
function addMaterial(e) {
    e.preventDefault();

    const title = document.getElementById('title').value.trim();
    const url = document.getElementById('url').value.trim();
    const type = document.getElementById('type').value;
    const description = document.getElementById('description').value.trim();

    if (!title) {
        alert('Vui lòng nhập tiêu đề tài liệu.');
        return;
    }

    const material = {
        id: currentId++,
        title,
        url,
        type,
        description
    };

    // Lưu `currentId` vào localStorage
    localStorage.setItem('currentId', JSON.stringify(currentId));

    materials.push(material);
    saveData();
    displayMaterials();
    resetForm();
}

// Hàm hiển thị danh sách tài liệu
function displayMaterials(filteredMaterials = null) {
    const materialList = document.getElementById('materialList');
    materialList.innerHTML = '';

    const displayData = filteredMaterials !== null ? filteredMaterials : materials;

    if (displayData.length === 0) {
        materialList.innerHTML = '<p class="text-center">Không có tài liệu nào.</p>';
        return;
    }

    displayData.forEach(material => {
        const materialItem = document.createElement('div');
        materialItem.className = 'list-group-item material-item';

        materialItem.innerHTML = `
            <div class="material-info">
                <h5 class="mb-1">${escapeHtml(material.title)} <span class="badge bg-secondary">${escapeHtml(material.type)}</span></h5>
                <p class="mb-1">${escapeHtml(material.description)}</p>
                ${material.url ? `<a href="${encodeURI(material.url)}" target="_blank">${escapeHtml(material.url)}</a>` : ''}
            </div>
            <div class="material-actions">
                <button class="btn btn-sm btn-outline-primary" onclick="editMaterial(${material.id})">Sửa</button>
                <button class="btn btn-sm btn-outline-danger" onclick="deleteMaterial(${material.id})">Xóa</button>
            </div>
        `;

        materialList.appendChild(materialItem);
    });
}

// Hàm reset biểu mẫu
function resetForm() {
    document.getElementById('materialForm').reset();
}

// Hàm lưu dữ liệu vào localStorage
function saveData() {
    localStorage.setItem('materials', JSON.stringify(materials));
    localStorage.setItem('currentId', JSON.stringify(currentId));
}

// Hàm lọc tài liệu dựa trên từ khóa và loại
function filterMaterials() {
    const keyword = document.getElementById('searchKeyword').value.toLowerCase();
    const type = document.getElementById('filterType').value;

    let filtered = materials.filter(material => {
        const matchesKeyword = material.title.toLowerCase().includes(keyword) || material.description.toLowerCase().includes(keyword);
        const matchesType = type === 'All' || material.type === type;
        return matchesKeyword && matchesType;
    });

    displayMaterials(filtered);
}

// Hàm xóa tất cả các bộ lọc
function clearFilters() {
    document.getElementById('searchKeyword').value = '';
    document.getElementById('filterType').value = 'All';
    displayMaterials();
}

// Hàm xóa một tài liệu dựa trên ID
function deleteMaterial(materialId) {
    if (confirm('Bạn có chắc chắn muốn xóa tài liệu này?')) {
        console.log('Deleting material with ID:', materialId);
        // Đảm bảo materialId là số
        materials = materials.filter(m => m.id !== Number(materialId));
        saveData();
        displayMaterials();
    }
}

// Hàm chỉnh sửa một tài liệu
function editMaterial(materialId) {
    const material = materials.find(m => m.id === Number(materialId));
    if (!material) return;

    // Pre-fill the form với dữ liệu hiện tại của tài liệu
    document.getElementById('title').value = material.title;
    document.getElementById('url').value = material.url;
    document.getElementById('type').value = material.type;
    document.getElementById('description').value = material.description;

    // Thêm một biến để lưu trữ trạng thái đang chỉnh sửa
    let isEditing = true;

    // Cập nhật sự kiện submit của form để cập nhật tài liệu thay vì thêm mới
    const form = document.getElementById('materialForm');
    form.removeEventListener('submit', addMaterial);
    form.addEventListener('submit', function updateMaterial(e) {
        e.preventDefault();

        // Cập nhật thông tin tài liệu
        material.title = document.getElementById('title').value.trim();
        material.url = document.getElementById('url').value.trim();
        material.type = document.getElementById('type').value;
        material.description = document.getElementById('description').value.trim();

        // Lưu dữ liệu và hiển thị lại danh sách
        saveData();
        displayMaterials();
        resetForm();

        // Khôi phục sự kiện submit ban đầu
        form.removeEventListener('submit', updateMaterial);
        form.addEventListener('submit', addMaterial);

        isEditing = false;
    });
}

// Hàm khởi tạo mẫu dữ liệu
function initializeSampleData() {
    const sampleMaterials = [
        {
            id: currentId++,
            title: 'Introduction to Java',
            url: 'https://www.w3schools.com/java/',
            type: 'Book',
            description: 'A comprehensive guide to Java programming.'
        },
        {
            id: currentId++,
            title: 'Understanding JSP',
            url: 'https://www.youtube.com/watch?v=HbMxNwNjQVw',
            type: 'Video',
            description: 'An introductory video about JSP technology.'
        },
        {
            id: currentId++,
            title: 'Learning HTML & CSS',
            url: 'https://www.w3schools.com/html/',
            type: 'Website',
            description: 'Tutorials for learning HTML and CSS.'
        },
        {
            id: currentId++,
            title: 'Advanced JavaScript',
            url: 'https://www.youtube.com/watch?v=PkZNo7MFNFg',
            type: 'Article',
            description: 'An article covering advanced JavaScript topics.'
        },
        // Thêm thêm 20 tài liệu mẫu từ các lĩnh vực khác nhau
        {
            id: currentId++,
            title: 'Calculus Made Easy',
            url: 'https://www.amazon.com/Calculus-Made-Easy-Stephen-Stewart/dp/0071816457',
            type: 'Book',
            description: 'A beginner-friendly book on calculus concepts.'
        },
        {
            id: currentId++,
            title: 'Quantum Mechanics Basics',
            url: 'https://www.khanacademy.org/science/physics/quantum-physics',
            type: 'Website',
            description: 'Khan Academy course on the fundamentals of quantum mechanics.'
        },
        {
            id: currentId++,
            title: 'Introduction to Algorithms',
            url: 'https://www.youtube.com/watch?v=RBSGKlAvoiM',
            type: 'Video',
            description: 'A comprehensive lecture series on algorithms by MIT.'
        },
        {
            id: currentId++,
            title: 'Modern Art Overview',
            url: 'https://www.tate.org.uk/art/art-terms/m/modern-art',
            type: 'Article',
            description: 'An overview of modern art movements and artists.'
        },
        {
            id: currentId++,
            title: 'Data Structures in C++',
            url: 'https://www.geeksforgeeks.org/data-structures/',
            type: 'Website',
            description: 'GeeksforGeeks tutorials on various data structures implemented in C++.'
        },
        {
            id: currentId++,
            title: 'The Great Gatsby',
            url: 'https://www.sparknotes.com/lit/gatsby/',
            type: 'Book',
            description: 'SparkNotes analysis and summaries of "The Great Gatsby".'
        },
        {
            id: currentId++,
            title: 'Introduction to Machine Learning',
            url: 'https://www.coursera.org/learn/machine-learning',
            type: 'Course',
            description: 'Andrew Ng’s course on Machine Learning on Coursera.'
        },
        {
            id: currentId++,
            title: 'Thermodynamics Principles',
            url: 'https://www.youtube.com/watch?v=HgPjbnGiSSE',
            type: 'Video',
            description: 'Lecture on thermodynamics principles by Professor Leonard.'
        },
        {
            id: currentId++,
            title: 'Shakespearean Literature',
            url: 'https://www.sparknotes.com/shakespeare/',
            type: 'Article',
            description: 'Analysis and summaries of Shakespeare’s plays.'
        },
        {
            id: currentId++,
            title: 'Linear Algebra Explained',
            url: 'https://www.khanacademy.org/math/linear-algebra',
            type: 'Website',
            description: 'Khan Academy resources on linear algebra.'
        },
        {
            id: currentId++,
            title: 'Astrophysics for People in a Hurry',
            url: 'https://www.amazon.com/Astrophysics-People-Hurry-Neil-deGrasse-Tyson/dp/0393609399',
            type: 'Book',
            description: 'A book by Neil deGrasse Tyson explaining astrophysics in an accessible way.'
        },
        {
            id: currentId++,
            title: 'Biology 101',
            url: 'https://www.khanacademy.org/science/biology',
            type: 'Website',
            description: 'Khan Academy’s introductory course on biology.'
        },
        {
            id: currentId++,
            title: 'History of the Renaissance',
            url: 'https://www.youtube.com/watch?v=6HjBi6B1_Vk',
            type: 'Video',
            description: 'Documentary on the Renaissance period.'
        },
        {
            id: currentId++,
            title: 'Introduction to Artificial Intelligence',
            url: 'https://www.coursera.org/learn/ai',
            type: 'Course',
            description: 'An introductory course on Artificial Intelligence.'
        },
        {
            id: currentId++,
            title: 'Organic Chemistry Basics',
            url: 'https://www.khanacademy.org/science/organic-chemistry',
            type: 'Website',
            description: 'Khan Academy’s resources on organic chemistry.'
        },
        {
            id: currentId++,
            title: 'Principles of Economics',
            url: 'https://www.youtube.com/watch?v=2r6Gx9zLZg4',
            type: 'Video',
            description: 'Lecture series on basic economic principles.'
        },
        {
            id: currentId++,
            title: 'World War II Overview',
            url: 'https://www.history.com/topics/world-war-ii/world-war-ii-history',
            type: 'Article',
            description: 'Comprehensive overview of World War II.'
        },
        {
            id: currentId++,
            title: 'Psychology 101',
            url: 'https://www.coursera.org/learn/psychology',
            type: 'Course',
            description: 'An introductory course on psychology.'
        }
    ];

    materials = sampleMaterials;
    saveData();
}

// Hàm bảo vệ chống XSS bằng cách escape các ký tự đặc biệt
function escapeHtml(text) {
    if (!text) return '';
    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}
