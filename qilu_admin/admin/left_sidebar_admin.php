<?php
/**
 * Created by PhpStorm.
 * User: barker81
 * Date: 10/06/18
 * Time: 15:25
 */

?>

<div class="left-sidebar">
    <!-- Sidebar scroll-->
    <div class="scroll-sidebar">
        <!-- Sidebar navigation-->
        <nav class="sidebar-nav">
            <ul id="sidebarnav">
                <!--<li class="nav-devider"></li>-->
                <li>
                    <a class="has-arrow  " href="../dashboard/panel.php" aria-expanded="false"><i class="fa fa-tachometer"></i><span class="hide-menu">Painel de controle </span></a>
                </li>
                <li class="nav-label">General</li>

                <li>
                    <a class="has-arrow  " href="../dashboard/installations.php" aria-expanded="false"><i class="fa fa-tablet"></i><span class="hide-menu">Instalations</span></a>
                </li>

                <li>
                    <a class="has-arrow " href="../dashboard/all_users.php" aria-expanded="false"><i class="fa fa-user"></i><span class="hide-menu">Users</span></a>
                    <ul aria-expanded="false" class="collapse">
                        <li><a href="../dashboard/all_users.php">All users</a></li>
                        <li><a href="../dashboard/admin_users.php">Admin users</a></li>
                    </ul>
                </li>

                <li class="nav-label">Features</li>
                <li>
                    <a class="has-arrow  " href="../dashboard/visits.php" aria-expanded="false"><i class="fa fa-comment-o"></i><span class="hide-menu">Connections</span></a>
                    <ul aria-expanded="false" class="collapse">
                        <li><a href="../dashboard/visits.php">Visits</a></li>
                        <li><a href="../dashboard/favorites.php">Favorites</a></li>
                        <li><a href="../dashboard/follow.php">Follow</a></li>
                    </ul>
                </li>
                <li>
                    <a class="has-arrow  " href="../dashboard/messages.php" aria-expanded="false"><i class="fa fa-comments-o"></i><span class="hide-menu">Messages</span></a>
                </li>
                <li>
                    <a class="has-arrow  " href="../dashboard/encounters.php" aria-expanded="false"><i class="fa fa-mars-double"></i><span class="hide-menu">Encounters</span></a>
                </li>

                <li>
                    <a class="has-arrow  " href="../dashboard/calls.php" aria-expanded="false"><i class="fa fa-phone"></i><span class="hide-menu">Calls</span></a>
                </li>
                <li>
                    <a class="has-arrow  " href="../dashboard/streaming.php" aria-expanded="false"><i class="fa fa-video-camera"></i><span class="hide-menu">Live Streams</span></a>
                </li>

                <li>
                    <a class="has-arrow  " href="../dashboard/gift.php" aria-expanded="false"><i class="fa fa-gift"></i><span class="hide-menu">Gifts</span></a>
                    <ul aria-expanded="false" class="collapse">
                        <li><a href="../dashboard/gift.php">All gifts</a></li>
                        <li><a href="../dashboard/add_gift.php">Add new gift</a></li>
                    </ul>
                </li>

                <li class="nav-label">Security</li>
                <li>
                    <a class="has-arrow  " href="../dashboard/report.php" aria-expanded="false"><i class="fa fa-flag"></i><span class="hide-menu">Reports</span></a>
                </li>

            </ul>
        </nav>
        <!-- End Sidebar navigation -->
    </div>
    <!-- End Sidebar scroll-->
</div>
