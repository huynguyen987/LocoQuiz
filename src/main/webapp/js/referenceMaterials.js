// referenceMaterials.js

// Khởi tạo danh sách tài liệu từ localStorage hoặc mảng trống
let materials = JSON.parse(localStorage.getItem('materials')) || [];

// Khởi tạo currentId từ localStorage hoặc đặt lại thành 1
let currentId = JSON.parse(localStorage.getItem('currentId')) || 1;

// Phân trang
let currentPage = 1;
const itemsPerPage = 4;

// Khởi tạo khi DOM đã được tải
document.addEventListener('DOMContentLoaded', function() {
    // Nếu chưa có dữ liệu, thêm một số mẫu dữ liệu
    if (materials.length === 0) {
        initializeSampleData();
    }
    displayMaterials();
    setupEventListeners();
    setupFormValidation();
    setupEditFormValidation();
});

// Thiết lập các sự kiện
function setupEventListeners() {
    const form = document.getElementById('materialForm');
    form.addEventListener('submit', addMaterial);

    document.getElementById('searchKeyword').addEventListener('input', function() {
        currentPage = 1; // Reset trang khi tìm kiếm
        filterMaterials();
    });
    document.getElementById('filterType').addEventListener('change', function() {
        currentPage = 1; // Reset trang khi lọc
        filterMaterials();
    });
    document.getElementById('clearFilters').addEventListener('click', function() {
        currentPage = 1; // Reset trang khi xóa lọc
        clearFilters();
    });

    // Thiết lập sự kiện cho form chỉnh sửa
    const editForm = document.getElementById('editMaterialForm');
    editForm.addEventListener('submit', updateMaterial);
}

// Hàm thiết lập xác thực cho form thêm mới
function setupFormValidation() {
    const form = document.getElementById('materialForm');
    form.addEventListener('submit', function(event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);
}

// Hàm thiết lập xác thực cho form chỉnh sửa
function setupEditFormValidation() {
    const form = document.getElementById('editMaterialForm');
    form.addEventListener('submit', function(event) {
        if (!form.checkValidity()) {
            event.preventDefault();
            event.stopPropagation();
        }
        form.classList.add('was-validated');
    }, false);
}

// Hàm thêm tài liệu mới
function addMaterial(e) {
    e.preventDefault();

    const form = document.getElementById('materialForm');
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }

    const title = document.getElementById('title').value.trim();
    const url = document.getElementById('url').value.trim();
    const type = document.getElementById('type').value;
    const description = document.getElementById('description').value.trim();

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

    // Hiển thị thông báo thành công
    showSuccessToast('Thêm tài liệu thành công!');
}

// Hàm hiển thị danh sách tài liệu với phân trang
function displayMaterials(filteredMaterials = null) {
    const materialList = document.getElementById('materialList');
    materialList.innerHTML = '';

    const data = filteredMaterials !== null ? filteredMaterials : materials;

    // Tính toán số lượng trang
    const totalPages = Math.ceil(data.length / itemsPerPage);

    // Lấy dữ liệu của trang hiện tại
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const paginatedData = data.slice(startIndex, endIndex);

    if (paginatedData.length === 0) {
        materialList.innerHTML = '<p class="text-center">Không có tài liệu nào.</p>';
    } else {
        paginatedData.forEach(material => {
            const materialItem = document.createElement('div');
            materialItem.className = 'list-group-item material-item';

            materialItem.innerHTML = `
                <div class="material-info">
                    <h5 class="mb-1">${escapeHtml(material.title)} <span class="badge bg-secondary">${escapeHtml(material.type)}</span></h5>
                    <p class="mb-1">${escapeHtml(material.description)}</p>
                    ${material.url ? `<a href="${encodeURI(material.url)}" target="_blank">${escapeHtml(material.url)}</a>` : ''}
                </div>
                <div class="material-actions">
                    <button class="btn btn-sm btn-outline-primary" onclick="openEditModal(${material.id})">Sửa</button>
                    <button class="btn btn-sm btn-outline-danger" onclick="deleteMaterial(${material.id})">Xóa</button>
                </div>
            `;

            materialList.appendChild(materialItem);
        });
    }

    // Hiển thị phân trang
    displayPaginationControls(data.length, totalPages);
}

// Hàm hiển thị phân trang
function displayPaginationControls(totalItems, totalPages) {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    if (totalPages <= 1) return; // Không cần phân trang nếu chỉ có 1 trang

    // Nút "Previous"
    const prevLi = document.createElement('li');
    prevLi.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    prevLi.innerHTML = `
        <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
        </a>
    `;
    prevLi.addEventListener('click', function(e) {
        e.preventDefault();
        if (currentPage > 1) {
            currentPage--;
            displayMaterials();
        }
    });
    pagination.appendChild(prevLi);

    // Các nút số trang
    for (let i = 1; i <= totalPages; i++) {
        const pageLi = document.createElement('li');
        pageLi.className = `page-item ${currentPage === i ? 'active' : ''}`;
        pageLi.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        pageLi.addEventListener('click', function(e) {
            e.preventDefault();
            currentPage = i;
            displayMaterials();
        });
        pagination.appendChild(pageLi);
    }

    // Nút "Next"
    const nextLi = document.createElement('li');
    nextLi.className = `page-item ${currentPage === totalPages ? 'disabled' : ''}`;
    nextLi.innerHTML = `
        <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
        </a>
    `;
    nextLi.addEventListener('click', function(e) {
        e.preventDefault();
        if (currentPage < totalPages) {
            currentPage++;
            displayMaterials();
        }
    });
    pagination.appendChild(nextLi);
}

// Hàm reset biểu mẫu
function resetForm() {
    const form = document.getElementById('materialForm');
    form.reset();
    form.classList.remove('was-validated');
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

// Hàm mở modal chỉnh sửa và điền thông tin tài liệu vào modal
function openEditModal(materialId) {
    const material = materials.find(m => m.id === Number(materialId));
    if (!material) return;

    // Điền thông tin vào modal
    document.getElementById('editMaterialId').value = material.id;
    document.getElementById('editTitle').value = material.title;
    document.getElementById('editUrl').value = material.url;
    document.getElementById('editType').value = material.type;
    document.getElementById('editDescription').value = material.description;

    // Reset xác thực modal trước khi hiển thị
    const editForm = document.getElementById('editMaterialForm');
    editForm.classList.remove('was-validated');

    // Mở modal
    const editModal = new bootstrap.Modal(document.getElementById('editMaterialModal'));
    editModal.show();
}

// Hàm cập nhật tài liệu sau khi chỉnh sửa
function updateMaterial(e) {
    e.preventDefault();

    const form = document.getElementById('editMaterialForm');
    if (!form.checkValidity()) {
        form.classList.add('was-validated');
        return;
    }

    const id = Number(document.getElementById('editMaterialId').value);
    const title = document.getElementById('editTitle').value.trim();
    const url = document.getElementById('editUrl').value.trim();
    const type = document.getElementById('editType').value;
    const description = document.getElementById('editDescription').value.trim();

    // Tìm vị trí của tài liệu trong mảng
    const index = materials.findIndex(m => m.id === id);
    if (index === -1) {
        alert('Không tìm thấy tài liệu để chỉnh sửa.');
        return;
    }

    // Cập nhật thông tin tài liệu
    materials[index].title = title;
    materials[index].url = url;
    materials[index].type = type;
    materials[index].description = description;

    saveData();
    displayMaterials();
    closeEditModal();

    // Hiển thị thông báo thành công
    showSuccessToast('Chỉnh sửa tài liệu thành công!');
}

// Hàm đóng modal chỉnh sửa
function closeEditModal() {
    const editModalEl = document.getElementById('editMaterialModal');
    const editModal = bootstrap.Modal.getInstance(editModalEl);
    editModal.hide();
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

// Hàm hiển thị Toast Notification
function showSuccessToast(message) {
    const toastEl = document.getElementById('successToast');
    const toastBody = toastEl.querySelector('.toast-body');
    toastBody.textContent = message;

    const toast = new bootstrap.Toast(toastEl, { delay: 3000 });
    toast.show();
}
