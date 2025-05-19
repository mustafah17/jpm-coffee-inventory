// app/page.tsx

'use client';
import React, { useState } from 'react';

// The backend API base URL
const API_BASE = 'http://localhost:8080/api/supply-items'

export default function SupplyItemApiTester() {
    // State for all forms and outputs
    const [allItems, setAllItems] = useState<any[]>([]);
    const [singleItem, setSingleItems] = useState<any>(null);
    const [searchResults, setSearchResults] = useState<any[]>([]);
    const [reorderList, setReorderList] = useState<any[]>([]);
    const [createResult, setCreateResult] = useState<any>(null);
    const [updateResult, setUpdateResult] = useState<any>(null);
    const [deleteResult, setDeleteResult] = useState<any>(null);
    const [quantityResult, setQuantityResult] = useState<any>(null);
    const [importResult, setImportResult] = useState<any>(null);

    // Form states
    const [itemId, setItemId] = useState('');
}