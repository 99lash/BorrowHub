<?php

namespace App\Http\Controllers\Api\V1;

use App\Http\Controllers\Controller;
use App\Services\DashboardService;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class DashboardController extends Controller
{
    protected $dashboardService;

    public function __construct(DashboardService $dashboardService)
    {
        $this->dashboardService = $dashboardService;
    }

    /**
     * Get dashboard statistics.
     */
    public function index(): JsonResponse
    {
        try {
            $stats = $this->dashboardService->getDashboardStats();

            return $this->successResponse(
                $stats,
                'Dashboard statistics retrieved successfully.'
            );
        } catch (\Exception $e) {
            return $this->errorResponse('An error occurred while retrieving dashboard statistics.', 500);
        }
    }
}
