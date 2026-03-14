<?php

namespace App\Services;

use App\Models\Item;
use App\Models\BorrowRecord;
use Illuminate\Support\Carbon;

class DashboardService
{
    /**
     * Get the dashboard statistics.
     *
     * @return array
     */
    public function getDashboardStats(): array
    {
        $totalItems = Item::sum('total_quantity');
        $availableNow = Item::sum('available_quantity');

        // Sum the borrowed quantities from active borrow records
        $currentlyBorrowed = BorrowRecord::where('status', 'borrowed')
            ->join('borrow_record_items', 'borrow_records.id', '=', 'borrow_record_items.borrow_record_id')
            ->sum('borrow_record_items.quantity');

        $dueTodayCount = BorrowRecord::where('status', 'borrowed')
            ->whereDate('due_at', Carbon::today())
            ->count();

        // Let's get recent transactions as well, similar to feature docs requirement
        $recentTransactions = BorrowRecord::with(['student', 'items'])
            ->orderBy('created_at', 'desc')
            ->take(5)
            ->get();

        return [
            'total_items' => (int) $totalItems,
            'currently_borrowed' => (int) $currentlyBorrowed,
            'available_now' => (int) $availableNow,
            'due_today' => $dueTodayCount,
            'recent_transactions' => $recentTransactions->map(function ($record) {
                return [
                    'id' => $record->id,
                    'student_name' => $record->student->name,
                    'student_number' => $record->student->student_number,
                    'items' => $record->items->map(function ($item) {
                        return [
                            'name' => $item->name,
                            'quantity' => $item->pivot->quantity,
                        ];
                    }),
                    'status' => $record->status,
                    'borrowed_at' => $record->borrowed_at,
                    'due_at' => $record->due_at,
                    'returned_at' => $record->returned_at,
                ];
            })
        ];
    }
}
